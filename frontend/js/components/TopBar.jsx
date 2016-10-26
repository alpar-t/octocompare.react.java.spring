import React from 'react';
import AppBar from 'material-ui/AppBar';
import {
  Toolbar, ToolbarGroup, ToolbarSeparator, ToolbarTitle,
} from 'material-ui/Toolbar';
import { Icon } from 'react-fa';
import FontIcon from 'material-ui/FontIcon';

export default () => <AppBar
  title="Joggr.io"
  iconElementRight={
    <Toolbar style={{ backgroundColor: 'inherit' }}>
      <ToolbarGroup>
        <FontIcon><Icon name="user" style={{ color: 'gray' }} /></FontIcon>
        <ToolbarTitle text="Alpar Torok" style={{ marginLeft: '0.5em' }} />
      </ToolbarGroup>
      <ToolbarGroup>
        <ToolbarSeparator />
        <FontIcon><Icon name="sign-out" style={{ color: 'gray' }} /></FontIcon>
      </ToolbarGroup>
    </Toolbar>
  }
  style={{
  }}
/>;
