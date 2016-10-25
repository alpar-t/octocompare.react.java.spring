import React from 'react';
import ReactDOM from 'react-dom';
import getMuiTheme from 'material-ui/styles/getMuiTheme';
import MuiThemeProvider from 'material-ui/styles/MuiThemeProvider';
import injectTapEventPlugin from 'react-tap-event-plugin';

import TopBar from 'joggr/components/TopBar'

// Needed for onTouchTap
// Check: https://github.com/zilverline/react-tap-event-plugin
injectTapEventPlugin();

/* global document */
ReactDOM.render(
  <MuiThemeProvider muiTheme={getMuiTheme()}>
    <TopBar />
  </MuiThemeProvider>,
  document.getElementById('app')
);
