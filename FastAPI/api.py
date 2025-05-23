from fastapi import FastAPI, Depends, HTTPException
from sqlalchemy import create_engine, Column, String, Integer, Date
from sqlalchemy.ext.declarative import declarative_base
from sqlalchemy.orm import sessionmaker, Session
from sqlalchemy.sql import text
import uvicorn
from pydantic import BaseModel
from typing import Optional,List
from fastapi.middleware.cors import CORSMiddleware
from sqlalchemy import inspect

DATABASE_URL = "YOURDATABASEURL"

# SQLAlchemy Settings
engine = create_engine(DATABASE_URL)
SessionLocal = sessionmaker(autocommit=False, autoflush=False, bind=engine)
Base = declarative_base()

app = FastAPI()

if __name__ == "__main__":
    uvicorn.run("api:app", host="0.0.0.0", port=8000)


class Work(Base):
    __tablename__ = "work"
    id = Column(Integer, primary_key=True, index=True)
    product = Column(String, index=True)
    project = Column(String, index=True)
    part = Column(String, index=True)
    plan_startdate = Column(String(10))
    plan_enddate = Column(String(10))
    real_startdate = Column(String(10))
    real_enddate = Column(String(10))

class User(Base):
    __tablename__ = "user"
    id = Column(Integer, primary_key=True, index=True)
    username = Column(String)
    password = Column(String)

# Pydantic models
class WorkCreate(BaseModel):
    product: str
    project: str
    part: str
    plan_startdate: Optional[str] = None
    plan_enddate: Optional[str] = None
    real_startdate: Optional[str] = None
    real_enddate: Optional[str] = None

class WorkResponse(WorkCreate):
    id: int

    class Config:
        orm_mode = True

class UserLogin(BaseModel):
    username: str
    password: str

class LoginResponse(BaseModel):
    success: bool
    message: str

# DB connect
def get_db():
    db = SessionLocal()
    try:
        yield db
    finally:
        db.close()

# Endpoints
@app.get("/items/", response_model=List[WorkResponse])
def get_all_work(db: Session = Depends(get_db)):
    return db.query(Work).all()

@app.get("/items/{id}", response_model=WorkResponse)
def get_work_by_id(id: int, db: Session = Depends(get_db)):
    work = db.query(Work).filter(Work.id == id).first()
    if not work:
        raise HTTPException(status_code=404, detail="Work not found")
    return work

@app.post("/items/", response_model=WorkResponse)
def create_work(work: WorkCreate, db: Session = Depends(get_db)):
    new_work = Work(**work.dict())
    db.add(new_work)
    db.commit()
    db.refresh(new_work)
    return new_work

@app.put("/items/{id}", response_model=WorkResponse)
def update_work(id: int, work: WorkCreate, db: Session = Depends(get_db)):
    existing = db.query(Work).filter(Work.id == id).first()
    if not existing:
        raise HTTPException(status_code=404, detail="Work not found")
    for key, value in work.dict().items():
        setattr(existing, key, value)
    db.commit()
    return existing

@app.delete("/items/{id}")
def delete_work(id: int, db: Session = Depends(get_db)):
    work = db.query(Work).filter(Work.id == id).first()
    if not work:
        raise HTTPException(status_code=404, detail="Work not found")
    db.delete(work)
    db.commit()
    return {"message": "Work deleted"}

@app.post("/login", response_model=LoginResponse)
def login(request: UserLogin, db: Session = Depends(get_db)):
    user = db.query(User).filter(User.username == request.username, User.password == request.password).first()
    if user:
        return {"success": True, "message": "Login successful"}
    return {"success": False, "message": "Invalid credentials"}

@app.get("/getTableNames")
def get_table_names():
    inspector = inspect(engine)
    return inspector.get_table_names()

@app.get("/getTableData/{table_name}")
def get_table_data(table_name: str):
    with engine.connect() as connection:
        result = connection.execute(text(f"SELECT * FROM `{table_name}`"))
        rows = [dict(row._mapping) for row in result]
    return {"data": rows}

@app.get("/get_columns/{table_name}")
def get_table_columns(table_name: str):
    inspector = inspect(engine)
    columns = inspector.get_columns(table_name)
    return {"columns": [column["name"] for column in columns]}

