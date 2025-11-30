from datetime import datetime
from typing import Optional

from pydantic import BaseModel, EmailStr, Field


class SocialLoginRequest(BaseModel):
    provider: str = Field(..., examples=["kakao", "apple"])
    provider_user_id: str
    email: EmailStr
    display_name: Optional[str] = None
    user_agent: Optional[str] = None
    device_name: Optional[str] = None
    ip_address: Optional[str] = None


class AuthTokens(BaseModel):
    access_token: str
    refresh_token: str
    token_type: str = "bearer"


class AuthResponse(BaseModel):
    user_id: str
    tokens: AuthTokens
    created_at: datetime
