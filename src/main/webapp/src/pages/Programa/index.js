import { Link } from "react-router-dom";
import React from "react";
import { Button, Card, Table, ButtonGroup } from "react-bootstrap";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {
  faEdit,
  faList,
  faTrash,
  faPlusCircle,
} from "@fortawesome/free-solid-svg-icons";
import api from "../../services/api";
import { useEffect } from "react";
import { useState } from "react";
import MyToast from "../../components/MyToast"

function Programa() {
  const [programas, setProgramas] = useState([]);
  const [show, setShow] = useState(false);

  useEffect(findAllProgramas);

  function findAllProgramas() {
    api
      .get("programa")
      .then((response) => response.data)
      .then((data) => {
        setProgramas(data);
      });
  }

  const deletePrograma = (id) => {
    api.delete(`programa/${id}`).then((response) => {
      if (response.data !== null) {
        setProgramas(programas.filter(programa => programa.id !== id));
        setShow(true);
        setTimeout(() => setShow(false), 3000);
      } else {
        setShow(false);
      }
    });
  };

  // const updatePrograma = (id) => {
  //   alert(id);
  // };

  return (
    <div>
      <div style={{ display: show ? "block" : "none" }}>
        <MyToast mostrarToast={{ show: show, menssagem: "Programa Excluído com SUCESSO!", type: "danger" }} />
      </div>
      <Card className={"border border-dark bg-dark text-white"}>
        <Card.Header>
          <FontAwesomeIcon icon={faList} />
          {"  "}Lista de Programas
        </Card.Header>
        <Card.Body>
          <Table bordered hover striped variant="dark">
            <thead>
              <tr>
                <th>Id</th>
                <th>Nome</th>
                <th>Ano</th>
                <th>Ações</th>
              </tr>
            </thead>
            <tbody>
              {programas.length === 0 ? (
                <tr align="center">
                  <td colSpan="4">Sem programas</td>
                </tr>
              ) : (
                programas.map((programa) => (
                  <tr key={programa.id}>
                    <td>{programa.id}</td>
                    <td>{programa.nome}</td>
                    <td>{programa.ano}</td>
                    <td>
                      <ButtonGroup
                        style={{
                          display: "flex",
                          justifyContent: "space-between",
                        }}
                      >
                        <Button size="md" variant="outline-primary">
                          <FontAwesomeIcon icon={faEdit} />
                        </Button>
                        <Button
                          size="md"
                          variant="outline-danger"
                          onClick={() => deletePrograma(programa.id)}
                        >
                          <FontAwesomeIcon icon={faTrash} />
                        </Button>
                      </ButtonGroup>
                    </td>
                  </tr>
                ))
              )}
            </tbody>
          </Table>
          <Link to={"/programa/form"} className="nav-link">
            <Button block>
              <FontAwesomeIcon icon={faPlusCircle} /> Novo Programa
            </Button>
          </Link>
        </Card.Body>
      </Card>
    </div>
  );
}

export default Programa;
