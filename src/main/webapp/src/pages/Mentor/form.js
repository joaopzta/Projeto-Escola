import React, { useEffect, useState } from "react";
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

function MentorForm(props) {
  const [id, setId] = useState("");
  const [nome, setNome] = useState("");
  const [pais, setPais] = useState("");
  const [show, setShow] = useState(false);
  const [metodo, setMetodo] = useState("");

  useEffect(() => {
    const mentorId = props.match.params.id;
    if (mentorId) {
      findMentorById(mentorId);
    }
  }, [props.match.params.id]);

  function findMentorById(id) {
    api
      .get(`mentor/${id}`)
      .then((response) => {
        if (response.data !== null) {
          setId(response.data.id);
          setPais(response.data.pais);
          setNome(response.data.nome);
        }
      })
      .catch((erro) => {
        console.log("Erro: " + erro);
      });
  }

  function submitForm(event) {
    event.preventDefault();
    const mentor = {
      nome: nome,
      pais: pais,
    };
    api.post("mentor", mentor).then((response) => {
      if (response.data !== null) {
        setPais("");
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
    const mentor = {
      nome: nome,
      pais: pais,
    };
    api.put(`mentor/${id}`, mentor).then((response) => {
      if (response.data !== null) {
        setId("");
        setPais("");
        setNome("");
        setShow(true);
        setMetodo("put");
        setTimeout(() => setShow(false), 3000);
        setTimeout(() => props.history.push("/mentor"), 3000);
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
          menssagem={
            metodo === "put"
              ? "Mentor Atualizado com SUCESSO!"
              : "Mentor Salvo com SUCESSO!"
          }
          type={"success"}
        />
      </div>
      <Card className={"border border-dark bg-dark text-white"}>
        <Card.Header>
          <FontAwesomeIcon icon={id ? faEdit : faPlusSquare} />{" "}
          {id ? "Atualizar Mentor" : "Novo Mentor"}
        </Card.Header>
        <Form onSubmit={id ? updateForm : submitForm} id="MentorFormId">
          <Card.Body>
            <Form.Row>
              <Form.Group as={Col}>
                <Form.Label>Nome</Form.Label>
                <Form.Control
                  required
                  value={nome}
                  onChange={(event) => {
                    setNome(event.target.value);
                  }}
                  type="text"
                  name="nome"
                  placeholder="Nome do Mentor"
                />
              </Form.Group>
              <Form.Group as={Col}>
                <Form.Label>País</Form.Label>
                <Form.Control
                  required
                  value={pais}
                  onChange={(event) => {
                    setPais(event.target.value);
                  }}
                  type="text"
                  name="pais"
                  placeholder="País de Origem"
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
                setPais("");
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

export default MentorForm;
