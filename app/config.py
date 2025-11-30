from pydantic import BaseModel, Field
from pydantic_settings import BaseSettings


class DatabaseSettings(BaseModel):
    url: str = Field(default="sqlite:///./app.db", description="SQLAlchemy database URL")


class Settings(BaseSettings):
    app_name: str = "Fit Founder API"
    database: DatabaseSettings = DatabaseSettings()


settings = Settings()
