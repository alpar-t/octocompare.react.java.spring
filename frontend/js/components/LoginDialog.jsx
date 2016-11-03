import React from 'react';
import Dialog from 'material-ui/Dialog';
import { colors } from 'material-ui/styles';
import { Tabs, Tab } from 'material-ui/Tabs';
import FlatButton from 'material-ui/FlatButton';
import Formsy from 'formsy-react';
import FormsyText from 'formsy-material-ui/lib/FormsyText';

const LoginDialog = ({ username, onLogin, onRegister }) => <Dialog
  modal
  open={username === ''}
  overlayStyle={{ backgroundColor: colors.grey50 }}
>
  <Tabs>
    <Tab label="Log-in">
      <Formsy.Form onValidSubmit={onLogin} >
        <FormsyText
          hintText="username"
          name="username"
          required
          updateImmediately
        /> <br />
        <FormsyText
          hintText="password"
          name="password"
          type="password"
          required
          updateImmediately
        /> <br />
        <FlatButton primary label="Log-in" type="submit" />
      </Formsy.Form>
    </Tab>
    <Tab label="Register" >
      <Formsy.Form onValidSubmit={onRegister} >
        <FormsyText
          hintText="username"
          name="username"
          required
          updateImmediately
        /> <br />
        <FormsyText
          hintText="password"
          name="password"
          type="password"
          required
          updateImmediately
        /> <br />
        <FlatButton primary label="Register" type="submit" />
      </Formsy.Form>
    </Tab>
  </Tabs>
</Dialog>;
LoginDialog.propTypes = {
  username: React.PropTypes.string,
  onRegister: React.PropTypes.func,
  onLogin: React.PropTypes.func,
};
export default LoginDialog;
