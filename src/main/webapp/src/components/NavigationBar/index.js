import React from "react";
import { Navbar, Nav } from "react-bootstrap";
import { Link } from "react-router-dom";

function NavigationBar() {
  return (
    <Navbar bg="dark" variant="dark">
      <Link to={"/"} className="navbar-brand">
        <Navbar.Brand>
          <img
            src="https://upload.wikimedia.org/wikipedia/commons/b/ba/Book_icon_1.png"
            width="25"
            height="25"
            alt="brand"
          />{" "}
          Escola
        </Navbar.Brand>
      </Link>
      <Nav className="mr-auto">
        <Link to={"/aluno"} className="nav-link">
          Alunos
        </Link>
        <Link to={"/mentor"} className="nav-link">
          Mentores
        </Link>
        <Link to={"/programa"} className="nav-link">
          Programas
        </Link>
        <Link to={"/materia"} className="nav-link">
          Materias
        </Link>
        <Link to={"/mentoria"} className="nav-link">
          Mentorias
        </Link>
      </Nav>
    </Navbar>
  );
}

export default NavigationBar;
