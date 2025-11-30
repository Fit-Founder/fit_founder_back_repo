from fastapi import APIRouter, Depends, HTTPException
from sqlalchemy.orm import Session

from app import models
from app.database import get_db
from app.schemas.user import UserCreate, UserResponse

router = APIRouter(prefix="/users", tags=["users"])


@router.post("", response_model=UserResponse, status_code=201)
def create_user(payload: UserCreate, db: Session = Depends(get_db)) -> UserResponse:
    existing = db.query(models.User).filter(models.User.user_id == payload.user_id).first()
    if existing:
        raise HTTPException(status_code=409, detail="User already exists")

    user = models.User(
        user_id=payload.user_id,
        nickname=payload.nickname,
        height_cm=payload.height_cm,
        weight_kg=payload.weight_kg,
        gender=payload.gender,
        profile_image_asset_id=payload.profile_image_asset_id,
        seen_tutorial_flag=False,
    )
    db.add(user)
    db.commit()
    db.refresh(user)
    return user


@router.get("/{user_id}", response_model=UserResponse)
def get_user(user_id: str, db: Session = Depends(get_db)) -> UserResponse:
    user = db.query(models.User).filter(models.User.user_id == user_id).first()
    if not user:
        raise HTTPException(status_code=404, detail="User not found")
    return user
