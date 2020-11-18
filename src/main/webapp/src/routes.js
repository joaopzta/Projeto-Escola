import { BrowserRouter, Switch, Route } from "react-router-dom";
import { Container, Row, Col } from "react-bootstrap";

import NavigationBar from "./pages/NavigationBar";
import Home from "./pages/Home";
import Aluno from "./pages/Aluno";
import Mentor from "./pages/Mentor";
import Programa from "./pages/Programa";
import ProgramaForm from "./pages/Programa/form"

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
              <Route path="/mentor" exact component={Mentor} />
              <Route path="/programa" exact component={Programa} />
              <Route path="/programa/form" exact component={ProgramaForm} />
            </Switch>
          </Col>
        </Row>
      </Container>
    </BrowserRouter>
  );
}

export default Routes;
