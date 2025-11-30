from datetime import datetime
from typing import List, Optional

from pydantic import BaseModel, Field


class ComparisonStartRequest(BaseModel):
    comparison_id: str
    user_id: str
    bestfit_id: str
    size_chart_id: str
    newfit_selected_row_id: Optional[str] = None


class DimensionDifferencePayload(BaseModel):
    key_code: str
    unit: str
    bestfit_value: float
    newfit_value: float
    delta: float


class ComparisonResponse(BaseModel):
    comparison_id: str
    status: str
    decision: Optional[str]
    created_at: datetime
    updated_at: datetime
    differences: List[DimensionDifferencePayload] = Field(default_factory=list)

    class Config:
        orm_mode = True
