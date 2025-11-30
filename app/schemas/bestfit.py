from datetime import datetime
from typing import Optional

from pydantic import BaseModel, Field


class BestFitCreate(BaseModel):
    bestfit_id: str
    user_id: str
    size_chart_id: str
    row_id: str
    product_name: str = Field(..., max_length=20)
    memo: Optional[str] = Field(None, max_length=100)
    favorite: bool = False
    pant_fit_type: Optional[str] = Field(None, examples=["wide", "straight", "slim"])


class BestFitResponse(BaseModel):
    bestfit_id: str
    user_id: str
    size_chart_id: str
    row_id: str
    product_name: str
    memo: Optional[str]
    favorite: bool
    pant_fit_type: Optional[str]
    created_at: datetime
    updated_at: datetime

    class Config:
        orm_mode = True
