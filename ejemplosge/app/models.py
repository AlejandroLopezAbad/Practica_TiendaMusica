from pydantic import BaseModel


# Esto es para manejar el encapsulamiento de los atributos
class Persona(BaseModel):
    id: int
    nombre: str
    apellido: str


class Departamento:
    id: int
    nombre: str


