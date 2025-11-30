from datetime import datetime
from typing import Optional

from pydantic import BaseModel, Field


class UserCreate(BaseModel):
    user_id: str = Field(..., description="Client-generated UUID for the user")
    nickname: str = Field(..., max_length=100)
    height_cm: int = Field(..., ge=100, le=230)
    weight_kg: int = Field(..., ge=20, le=250)
    gender: str = Field(..., examples=["male", "female"])
    profile_image_asset_id: Optional[str] = None


class UserResponse(BaseModel):
    user_id: str
    nickname: str
    height_cm: int
    weight_kg: int
    gender: str
    seen_tutorial_flag: bool
    created_at: datetime
    updated_at: datetime

    class Config:
        orm_mode = True
