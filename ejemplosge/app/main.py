from fastapi import FastAPI

from .models import Persona

app = FastAPI()
personas = [
    Persona(id=0, nombre="Javier", apellido="Palacios"),
    Persona(id=1, nombre="Carmen", apellido=""),
    Persona(id=2, nombre="Jose luis",apellido=""),
    Persona(id=3, nombre="Teresa", apellido=""),
    Persona(id=4, nombre="Yolanda", apellido="")
]


@app.get("/personas/")
def read_root():
    return personas


@app.get("/personas/{persona_id}")
def read_persona(persona_id: int):
    return personas[persona_id]
