import {
  faEdit,
  faPlusSquare,
  faSave,
  faUndo,
} from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import React, { useEffect, useState } from "react";
import { Button, Card, Form } from "react-bootstrap";
import MyToast from "../../components/MyToast";
import api from "../../services/api";

function MateriaForm(props) {
  const [id, setId] = useState("");
  const [descricao, setDescricao] = useState("");
  const [show, setShow] = useState(false);
  const [metodo, setMetodo] = useState("");

  useEffect(() => {
    const materiaId = props.match.params.id;
    if (materiaId) {
      findMateriaById(materiaId);
    }
  }, [props.match.params.id]);

  function findMateriaById(id) {
    api
      .get(`materia/${id}`)
      .then((response) => {
        if (response.data !== null) {
          setId(response.data.id);
          setDescricao(response.data.descricao);
        }
      })
      .catch((erro) => {
        console.log("Erro: " + erro);
      });
  }

  function submitForm(event) {
    event.preventDefault();
    const materia = {
      descricao: descricao,
    };
    api.post("materia", materia).then((response) => {
      if (response.data !== null) {
        setDescricao("");
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
    const materia = {
      descricao: descricao,
    };
    api.put(`materia/${id}`, materia).then((response) => {
      if (response.data !== null) {
        setId("");
        setDescricao("");
        setShow(true);
        setMetodo("put");
        setTimeout(() => setShow(false), 3000);
        setTimeout(() => props.history.push("/materia"), 3000);
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
              ? "Matéria Atualizada com SUCESSO!"
              : "Matéria Salva com SUCESSO!"
          }
          type={"success"}
        />
      </div>
      <Card className={"border border-dark bg-dark text-white"}>
        <Card.Header>
          <FontAwesomeIcon icon={id ? faEdit : faPlusSquare} />{" "}
          {id ? "Atualizar Matéria" : "Novo Matéria"}
        </Card.Header>
        <Form onSubmit={id ? updateForm : submitForm} id="MateriaFormId">
          <Card.Body>
            <Form.Row>
              <Form.Label>Matéria</Form.Label>
              <Form.Control
                required
                value={descricao}
                onChange={(event) => {
                  setDescricao(event.target.value);
                }}
                type="text"
                name="descricao"
                placeholder="Descrição"
              />
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
                setDescricao("");
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

export default MateriaForm;
