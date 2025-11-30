from datetime import datetime, timedelta
from uuid import uuid4

from fastapi import APIRouter, Depends, HTTPException, status
from sqlalchemy.orm import Session

from app import models
from app.database import get_db
from app.schemas.auth import AuthResponse, AuthTokens, SocialLoginRequest

router = APIRouter(prefix="/auth", tags=["auth"])


def _generate_tokens(subject: str) -> AuthTokens:
    now = datetime.utcnow()
    access_token = f"access-{subject}-{int(now.timestamp())}"
    refresh_token = f"refresh-{subject}-{int((now + timedelta(days=30)).timestamp())}"
    return AuthTokens(access_token=access_token, refresh_token=refresh_token)


@router.post("/social-login", response_model=AuthResponse)
def social_login(payload: SocialLoginRequest, db: Session = Depends(get_db)) -> AuthResponse:
    social = (
        db.query(models.SocialAccount)
        .filter(
            models.SocialAccount.provider == payload.provider,
            models.SocialAccount.provider_user_id == payload.provider_user_id,
        )
        .first()
    )

    if social:
        tokens = _generate_tokens(social.user_id)
        return AuthResponse(user_id=social.user_id, tokens=tokens, created_at=datetime.utcnow())

    user = models.User(
        user_id=str(uuid4()),
        nickname=payload.display_name or "신규 사용자",
        height_cm=170,
        weight_kg=60,
        gender="male",
        seen_tutorial_flag=False,
    )
    db.add(user)

    social_account = models.SocialAccount(
        social_account_id=str(uuid4()),
        user_id=user.user_id,
        provider=payload.provider,
        provider_user_id=payload.provider_user_id,
        email=payload.email,
        display_name=payload.display_name,
        user_agent=payload.user_agent,
        device_name=payload.device_name,
        ip_address=payload.ip_address,
        connected_at=datetime.utcnow(),
    )
    db.add(social_account)

    email_row = models.UserEmail(
        email_id=str(uuid4()),
        user_id=user.user_id,
        social_account_id=social_account.social_account_id,
        is_primary=True,
        verified=True,
        source="social",
        provider=payload.provider,
        verified_at=datetime.utcnow(),
    )
    db.add(email_row)

    tokens = _generate_tokens(user.user_id)

    db.commit()
    return AuthResponse(user_id=user.user_id, tokens=tokens, created_at=datetime.utcnow())
