from datetime import datetime
from enum import Enum

from sqlalchemy import Boolean, Column, DateTime, ForeignKey, Integer, Numeric, String
from sqlalchemy.orm import declarative_base, relationship

Base = declarative_base()


class GenderEnum(str, Enum):
    male = "male"
    female = "female"


class SocialProvider(str, Enum):
    kakao = "kakao"
    apple = "apple"


class LinkStatus(str, Enum):
    pending = "pending"
    linked = "linked"
    declined = "declined"
    expired = "expired"


class ComparisonStatus(str, Enum):
    queued = "queued"
    running = "running"
    succeeded = "succeeded"
    failed = "failed"


class ComparisonDecision(str, Enum):
    recommended = "RECOMMENDED"
    uncertain = "UNCERTAIN"
    not_recommended = "NOT_RECOMMENDED"


class MeasurementUnit(str, Enum):
    cm = "cm"
    inch = "inch"


class PantsFitType(str, Enum):
    wide = "wide"
    straight = "straight"
    slim = "slim"


class TimestampMixin:
    created_at = Column(DateTime, default=datetime.utcnow, nullable=False)
    updated_at = Column(DateTime, default=datetime.utcnow, onupdate=datetime.utcnow, nullable=False)


class User(Base, TimestampMixin):
    __tablename__ = "users"

    user_id = Column(String(40), primary_key=True)
    nickname = Column(String(100), nullable=False)
    height_cm = Column(Integer, nullable=False)
    weight_kg = Column(Integer, nullable=False)
    profile_image_asset_id = Column(String(255))
    gender = Column(String(10), nullable=False)
    last_link_at = Column(DateTime)
    seen_tutorial_flag = Column(Boolean, default=False, nullable=False)
    primary_email_id = Column(String(50))

    social_accounts = relationship("SocialAccount", back_populates="user", cascade="all, delete-orphan")
    emails = relationship("UserEmail", back_populates="user", cascade="all, delete-orphan")
    sessions = relationship("UserSession", back_populates="user", cascade="all, delete-orphan")
    bestfits = relationship("BestFit", back_populates="user", cascade="all, delete-orphan")
    comparisons = relationship("Comparison", back_populates="user", cascade="all, delete-orphan")


class SocialAccount(Base, TimestampMixin):
    __tablename__ = "social_accounts"

    social_account_id = Column(String, primary_key=True)
    user_id = Column(String(40), ForeignKey("users.user_id"), nullable=False)
    link_flow_id = Column(String)
    provider = Column(String, nullable=False)
    provider_user_id = Column(String, nullable=False)
    email = Column(String, nullable=False)
    email_verified = Column(Boolean)
    email_updated_at = Column(DateTime)
    display_name = Column(String)
    provider_scope = Column(String)
    connected_at = Column(DateTime, default=datetime.utcnow, nullable=False)
    revoked_at = Column(DateTime)

    user = relationship("User", back_populates="social_accounts")


class AccountLinkFlow(Base, TimestampMixin):
    __tablename__ = "account_link_flows"

    link_flow_id = Column(String, primary_key=True)
    candidate_user_id = Column(String, ForeignKey("users.user_id"), nullable=False)
    matched_email_id = Column(String)
    status = Column(String, default=LinkStatus.pending.value, nullable=False)
    provider = Column(String, nullable=False)
    provider_user_id = Column(String, nullable=False)
    provider_email = Column(String)
    decision = Column(String)
    decided_at = Column(DateTime)
    result_social_account_id = Column(String)
    expires_at = Column(DateTime, nullable=False)
    ip_address = Column(String)
    user_agent = Column(String)

    candidate_user = relationship("User")


class UserEmail(Base, TimestampMixin):
    __tablename__ = "user_emails"

    email_id = Column(String, primary_key=True)
    user_id = Column(String(40), ForeignKey("users.user_id"), nullable=False)
    social_account_id = Column(String, ForeignKey("social_accounts.social_account_id"), nullable=False)
    is_primary = Column(Boolean, default=False, nullable=False)
    verified = Column(Boolean, default=False, nullable=False)
    source = Column(String, default="social", nullable=False)
    provider = Column(String)
    verified_at = Column(DateTime)

    user = relationship("User", back_populates="emails")
    social_account = relationship("SocialAccount")


class UserSession(Base):
    __tablename__ = "user_sessions"

    session_id = Column(String(40), primary_key=True)
    user_id = Column(String(40), ForeignKey("users.user_id"), nullable=False)
    refresh_token_hash = Column(String(255), nullable=False)
    user_agent = Column(String)
    device_name = Column(String)
    ip_address = Column(String)
    created_at = Column(DateTime, default=datetime.utcnow, nullable=False)
    expires_at = Column(DateTime, nullable=False)
    revoked_at = Column(DateTime)

    user = relationship("User", back_populates="sessions")


class SizeChart(Base, TimestampMixin):
    __tablename__ = "size_charts"

    size_chart_id = Column(String, primary_key=True)
    job_id2 = Column(String)
    garment_type = Column(String, nullable=False)
    unit = Column(String, nullable=False)
    source = Column(String, nullable=False)

    rows = relationship("SizeChartRow", back_populates="size_chart", cascade="all, delete-orphan")


class SizeChartRow(Base):
    __tablename__ = "size_chart_rows"

    row_id = Column(String, primary_key=True)
    size_chart_id = Column(String, ForeignKey("size_charts.size_chart_id"), nullable=False)
    size_label = Column(String, nullable=False)
    ordinal = Column(Integer, default=1, nullable=False)

    size_chart = relationship("SizeChart", back_populates="rows")
    measurements = relationship("MeasurementValue", back_populates="row", cascade="all, delete-orphan")


class MeasurementKey(Base):
    __tablename__ = "measurement_keys"

    key_code = Column(String, primary_key=True)
    garment_type = Column(String, nullable=False)
    display_name = Column(String, nullable=False)
    sort_order = Column(Integer, nullable=False)

    values = relationship("MeasurementValue", back_populates="key")
    differences = relationship("DimensionDifference", back_populates="key")


class MeasurementValue(Base):
    __tablename__ = "measurement_values"

    row_id = Column(String, ForeignKey("size_chart_rows.row_id"), primary_key=True)
    key_code = Column(String, ForeignKey("measurement_keys.key_code"), primary_key=True)
    value = Column(Numeric, nullable=False)

    row = relationship("SizeChartRow", back_populates="measurements")
    key = relationship("MeasurementKey", back_populates="values")


class BestFit(Base, TimestampMixin):
    __tablename__ = "bestfits"

    bestfit_id = Column(String, primary_key=True)
    user_id = Column(String(40), ForeignKey("users.user_id"), nullable=False)
    size_chart_id = Column(String, ForeignKey("size_charts.size_chart_id"), nullable=False)
    row_id = Column(String, ForeignKey("size_chart_rows.row_id"), nullable=False)
    product_name = Column(String, nullable=False)
    memo = Column(String)
    favorite = Column(Boolean, default=False, nullable=False)
    pant_fit_type = Column(String)

    user = relationship("User", back_populates="bestfits")
    size_chart = relationship("SizeChart")
    row = relationship("SizeChartRow")
    comparisons = relationship("Comparison", back_populates="bestfit")


class Comparison(Base, TimestampMixin):
    __tablename__ = "comparisons"

    comparison_id = Column(String, primary_key=True)
    user_id = Column(String(40), ForeignKey("users.user_id"), nullable=False)
    bestfit_id = Column(String, ForeignKey("bestfits.bestfit_id"), nullable=False)
    size_chart_id = Column(String, ForeignKey("size_charts.size_chart_id"), nullable=False)
    newfit_selected_row_id = Column(String, ForeignKey("size_chart_rows.row_id"))
    status = Column(String, nullable=False)
    decision = Column(String)

    user = relationship("User", back_populates="comparisons")
    bestfit = relationship("BestFit", back_populates="comparisons")
    size_chart = relationship("SizeChart")
    newfit_row = relationship("SizeChartRow")
    differences = relationship("DimensionDifference", back_populates="comparison", cascade="all, delete-orphan")


class DimensionDifference(Base):
    __tablename__ = "dimension_differences"

    comparison_id = Column(String, ForeignKey("comparisons.comparison_id"), primary_key=True)
    key_code = Column(String, ForeignKey("measurement_keys.key_code"), primary_key=True)
    unit = Column(String, nullable=False)
    bestfit_value = Column(Numeric, nullable=False)
    newfit_value = Column(Numeric, nullable=False)
    delta = Column(Numeric, nullable=False)

    comparison = relationship("Comparison", back_populates="differences")
    key = relationship("MeasurementKey", back_populates="differences")


class Asset(Base, TimestampMixin):
    __tablename__ = "assets"

    asset_id = Column(String(40), primary_key=True)
    owner_type = Column(String)
    owner_id = Column(String)
    content_type = Column(String, nullable=False)
    width = Column(Integer)
    height = Column(Integer)
    uri = Column(String, nullable=False)


class OutlineComposition(Base):
    __tablename__ = "outline_compositions"

    comparison_id = Column(String, ForeignKey("comparisons.comparison_id"), primary_key=True)
    asset_id = Column(String(40), ForeignKey("assets.asset_id"), primary_key=True)
    role = Column(String, nullable=False)

    comparison = relationship("Comparison")
    asset = relationship("Asset")
