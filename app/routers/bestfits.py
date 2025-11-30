from datetime import datetime

from fastapi import APIRouter, Depends, HTTPException
from sqlalchemy.orm import Session

from app import models
from app.database import get_db
from app.schemas.bestfit import BestFitCreate, BestFitResponse

router = APIRouter(prefix="/bestfits", tags=["bestfits"])


@router.post("", response_model=BestFitResponse, status_code=201)
def create_bestfit(payload: BestFitCreate, db: Session = Depends(get_db)) -> BestFitResponse:
    user = db.query(models.User).filter(models.User.user_id == payload.user_id).first()
    if not user:
        raise HTTPException(status_code=404, detail="User not found")

    size_chart = db.query(models.SizeChart).filter(models.SizeChart.size_chart_id == payload.size_chart_id).first()
    if not size_chart:
        size_chart = models.SizeChart(
            size_chart_id=payload.size_chart_id,
            garment_type="pants",
            unit="cm",
            source="manual",
            created_at=datetime.utcnow(),
            updated_at=datetime.utcnow(),
        )
        db.add(size_chart)

    row = db.query(models.SizeChartRow).filter(models.SizeChartRow.row_id == payload.row_id).first()
    if not row:
        row = models.SizeChartRow(
            row_id=payload.row_id,
            size_chart_id=size_chart.size_chart_id,
            size_label="FREE",
            ordinal=1,
        )
        db.add(row)

    bestfit = models.BestFit(
        bestfit_id=payload.bestfit_id,
        user_id=payload.user_id,
        size_chart_id=size_chart.size_chart_id,
        row_id=row.row_id,
        product_name=payload.product_name,
        memo=payload.memo,
        favorite=payload.favorite,
        pant_fit_type=payload.pant_fit_type,
        created_at=datetime.utcnow(),
        updated_at=datetime.utcnow(),
    )

    if payload.favorite:
        for existing in user.bestfits:
            existing.favorite = False

    db.add(bestfit)
    db.commit()
    db.refresh(bestfit)
    return bestfit


@router.get("", response_model=list[BestFitResponse])
def list_bestfits(user_id: str, db: Session = Depends(get_db)) -> list[BestFitResponse]:
    return db.query(models.BestFit).filter(models.BestFit.user_id == user_id).all()
