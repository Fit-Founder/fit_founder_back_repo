from datetime import datetime

from fastapi import APIRouter, Depends, HTTPException
from sqlalchemy.orm import Session

from app import models
from app.database import get_db
from app.schemas.comparison import ComparisonResponse, ComparisonStartRequest, DimensionDifferencePayload

router = APIRouter(prefix="/comparisons", tags=["comparisons"])


@router.post("", response_model=ComparisonResponse, status_code=201)
def start_comparison(payload: ComparisonStartRequest, db: Session = Depends(get_db)) -> ComparisonResponse:
    user = db.query(models.User).filter(models.User.user_id == payload.user_id).first()
    if not user:
        raise HTTPException(status_code=404, detail="User not found")

    bestfit = db.query(models.BestFit).filter(models.BestFit.bestfit_id == payload.bestfit_id).first()
    if not bestfit:
        raise HTTPException(status_code=400, detail="BestFit is required to start comparison")

    comparison = models.Comparison(
        comparison_id=payload.comparison_id,
        user_id=user.user_id,
        bestfit_id=bestfit.bestfit_id,
        size_chart_id=payload.size_chart_id,
        newfit_selected_row_id=payload.newfit_selected_row_id,
        status=models.ComparisonStatus.running.value,
        decision=None,
        created_at=datetime.utcnow(),
        updated_at=datetime.utcnow(),
    )
    db.add(comparison)
    db.commit()
    db.refresh(comparison)

    return ComparisonResponse(
        comparison_id=comparison.comparison_id,
        status=comparison.status,
        decision=comparison.decision,
        created_at=comparison.created_at,
        updated_at=comparison.updated_at,
        differences=[],
    )


@router.post("/{comparison_id}/differences", response_model=ComparisonResponse)
def add_differences(
    comparison_id: str,
    differences: list[DimensionDifferencePayload],
    db: Session = Depends(get_db),
) -> ComparisonResponse:
    comparison = db.query(models.Comparison).filter(models.Comparison.comparison_id == comparison_id).first()
    if not comparison:
        raise HTTPException(status_code=404, detail="Comparison not found")

    comparison.differences.clear()
    for diff in differences:
        comparison.differences.append(
            models.DimensionDifference(
                comparison_id=comparison_id,
                key_code=diff.key_code,
                unit=diff.unit,
                bestfit_value=diff.bestfit_value,
                newfit_value=diff.newfit_value,
                delta=diff.delta,
            )
        )
    comparison.status = models.ComparisonStatus.succeeded.value
    comparison.updated_at = datetime.utcnow()

    db.add(comparison)
    db.commit()
    db.refresh(comparison)

    return ComparisonResponse(
        comparison_id=comparison.comparison_id,
        status=comparison.status,
        decision=comparison.decision,
        created_at=comparison.created_at,
        updated_at=comparison.updated_at,
        differences=[
            DimensionDifferencePayload(
                key_code=d.key_code,
                unit=d.unit,
                bestfit_value=float(d.bestfit_value),
                newfit_value=float(d.newfit_value),
                delta=float(d.delta),
            )
            for d in comparison.differences
        ],
    )


@router.get("/{comparison_id}", response_model=ComparisonResponse)
def get_comparison(comparison_id: str, db: Session = Depends(get_db)) -> ComparisonResponse:
    comparison = db.query(models.Comparison).filter(models.Comparison.comparison_id == comparison_id).first()
    if not comparison:
        raise HTTPException(status_code=404, detail="Comparison not found")

    return ComparisonResponse(
        comparison_id=comparison.comparison_id,
        status=comparison.status,
        decision=comparison.decision,
        created_at=comparison.created_at,
        updated_at=comparison.updated_at,
        differences=[
            DimensionDifferencePayload(
                key_code=d.key_code,
                unit=d.unit,
                bestfit_value=float(d.bestfit_value),
                newfit_value=float(d.newfit_value),
                delta=float(d.delta),
            )
            for d in comparison.differences
        ],
    )
