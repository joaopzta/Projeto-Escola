import React from "react";
import { useState } from "react";
import { Button, Card, Form, Col } from "react-bootstrap";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {
  faPlusSquare,
  faSave,
  faUndo,
} from "@fortawesome/free-solid-svg-icons";
import MyToast from "../../components/MyToast";
import api from "../../services/api";

function ProgramaForm() {
  const [nome, setNome] = useState("");
  const [ano, setAno] = useState("");
  const [show, setShow] = useState(false);

  function submitForm(event) {
    event.preventDefault();
    const programa = {
      nome: nome,
      ano: ano,
    };
    api.post("programa", programa).then((response) => {
      if (response.data !== null) {
        setAno("");
        setNome("");
        setShow(true);
        setTimeout(() => setShow(false), 3000);
      } else {
        setShow(false);
      }
    });
  }

  return (
    <div>
      <div style={{ display: show ? "block" : "none" }}>
        <MyToast mostrarToast={{ show: show, menssagem: "Programa Salvo com SUCESSO!", type: "success" }} />
      </div>
      <Card className={"border border-dark bg-dark text-white"}>
        <Card.Header>
          <FontAwesomeIcon icon={faPlusSquare} /> Formulário
        </Card.Header>
        <Form onSubmit={submitForm} id="programaFormId">
          <Card.Body>
            <Form.Row>
              <Form.Group as={Col}>
                <Form.Label>Descrição</Form.Label>
                <Form.Control
                  required
                  value={nome}
                  onChange={(event) => {
                    setNome(event.target.value);
                  }}
                  type="text"
                  name="nome"
                  placeholder="Nome do Programa"
                />
              </Form.Group>
              <Form.Group as={Col}>
                <Form.Label>Ano</Form.Label>
                <Form.Control
                  required
                  value={ano}
                  onChange={(event) => {
                    setAno(event.target.value);
                  }}
                  type="date"
                  name="ano"
                />
              </Form.Group>
            </Form.Row>
          </Card.Body>
          <Card.Footer style={{ textAlign: "right" }}>
            <Button variant="success" type="submit">
              <FontAwesomeIcon icon={faSave} /> Submit
            </Button>
            {"  "}
            <Button
              variant="info"
              type="submit"
              onClick={() => {
                setNome("");
                setAno("");
              }}
            >
              <FontAwesomeIcon icon={faUndo} /> Reset
            </Button>
          </Card.Footer>
        </Form>
      </Card>
    </div>
  );
}

export default ProgramaForm;
