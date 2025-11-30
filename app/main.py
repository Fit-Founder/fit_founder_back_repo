from fastapi import FastAPI

from app.database import engine
from app import models
from app.routers import auth, bestfits, comparisons, users

models.Base.metadata.create_all(bind=engine)

app = FastAPI(title="Fit Founder API", version="0.1.0")

app.include_router(auth.router)
app.include_router(users.router)
app.include_router(bestfits.router)
app.include_router(comparisons.router)


@app.get("/health")
def health() -> dict[str, str]:
    return {"status": "ok"}
