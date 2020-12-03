import { BrowserRouter, Switch, Route } from "react-router-dom";
import { Container, Row, Col } from "react-bootstrap";

import NavigationBar from "./components/NavigationBar";
import Home from "./pages/Home";
import Aluno from "./pages/Aluno";
import AlunoForm from "./pages/Aluno/form";
import Mentor from "./pages/Mentor";
import MentorForm from "./pages/Mentor/form";
import Programa from "./pages/Programa";
import ProgramaForm from "./pages/Programa/form";
import Materia from "./pages/Materia";
import MateriaForm from "./pages/Materia/form";
import MentoriaAluno from "./pages/Mentoria/aluno";
import MentoriaMentor from "./pages/Mentoria/mentor";
import Mentoria from "./pages/Mentoria";

function Routes() {
  return (
    <BrowserRouter>
      <NavigationBar />
      <Container>
        <Row>
          <Col lg={12} style={{ marginTop: "20px" }}>
            <Switch>
              <Route path="/" exact component={Home} />
              <Route path="/aluno" exact component={Aluno} />
              <Route path="/aluno/form" exact component={AlunoForm} />
              <Route path="/aluno/form/:id" exact component={AlunoForm} />
              <Route path="/mentor" exact component={Mentor} />
              <Route path="/mentor/form" exact component={MentorForm} />
              <Route path="/mentor/form/:id" exact component={MentorForm} />
              <Route path="/programa" exact component={Programa} />
              <Route path="/programa/form" exact component={ProgramaForm} />
              <Route path="/programa/form/:id" exact component={ProgramaForm} />
              <Route path="/materia" exact component={Materia} />
              <Route path="/materia/form" exact component={MateriaForm} />
              <Route path="/materia/form/:id" exact component={MateriaForm} />
              <Route path="/mentoria" exact component={Mentoria} />
              <Route path="/aluno/:id/mentoria" exact component={MentoriaAluno} />
              <Route path="/mentor/:id/mentoria" exact component={MentoriaMentor} />
            </Switch>
          </Col>
        </Row>
      </Container>
    </BrowserRouter>
  );
}

export default Routes;
