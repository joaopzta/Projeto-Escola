import React, { useEffect } from "react";
import { useState } from "react";
import { Button, Card, Form, Col } from "react-bootstrap";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {
  faEdit,
  faPlusSquare,
  faSave,
  faUndo,
} from "@fortawesome/free-solid-svg-icons";
import MyToast from "../../components/MyToast";
import api from "../../services/api";

function ProgramaForm(props) {
  const [id, setId] = useState("");
  const [nome, setNome] = useState("");
  const [ano, setAno] = useState("");
  const [show, setShow] = useState(false);
  const [metodo, setMetodo] = useState("");
  
  useEffect(() => {
    const programaId = props.match.params.id;
    if (programaId) {
      findProgramById(programaId);
    }
  }, [props.match.params.id]);

  function findProgramById(id) {
    api
      .get(`programa/${id}`)
      .then((response) => {
        if (response.data !== null) {
          setId(response.data.id);
          setAno(response.data.ano);
          setNome(response.data.nome);
        }
      })
      .catch((erro) => {
        console.log("Erro: " + erro);
      });
  }

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
        setMetodo("post");
        setTimeout(() => setShow(false), 3000);
      } else {
        setShow(false);
      }
    });
  }

  function updateForm(event) {
    event.preventDefault();
    const programa = {
      nome: nome,
      ano: ano,
    };
    api.put(`programa/${id}`, programa).then((response) => {
      if (response.data !== null) {
        setId("");
        setAno("");
        setNome("");
        setShow(true);
        setMetodo("put");
        setTimeout(() => setShow(false), 3000);
        setTimeout(() => props.history.push("/programa"), 3000);
      } else {
        setShow(false);
      }
    });
  }

  return (
    <div>
      <div style={{ display: show ? "block" : "none" }}>
        <MyToast
          show={show}
          menssagem={metodo === "put" ? "Programa Atualizado com SUCESSO!" : "Programa Salvo com SUCESSO!"}
          type={"success"}
        />
      </div>
      <Card className={"border border-dark bg-dark text-white"}>
        <Card.Header>
          <FontAwesomeIcon icon={id ? faEdit : faPlusSquare} />{" "}
          {id ? "Atualizar Programa" : "Novo Programa"}
        </Card.Header>
        <Form onSubmit={id ? updateForm : submitForm} id="programaFormId">
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
              <FontAwesomeIcon icon={faSave} /> {id ? "Update" : "Save"}
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
