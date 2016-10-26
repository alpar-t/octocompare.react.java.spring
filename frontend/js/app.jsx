import React from 'react';
import ReactDOM from 'react-dom';
import getMuiTheme from 'material-ui/styles/getMuiTheme';
import MuiThemeProvider from 'material-ui/styles/MuiThemeProvider';
import injectTapEventPlugin from 'react-tap-event-plugin';
import moment from 'moment';

import TopBar from 'joggr/components/TopBar';
import JoggingList from 'joggr/components/JoggingList';

// Needed for onTouchTap
// Check: https://github.com/zilverline/react-tap-event-plugin
injectTapEventPlugin();

const AppTemplate = () => <div style={{ maxWidth: '1200px' }}>
  <TopBar />
  <JoggingList
    activities={[
      {
        id: '1',
        date: moment().add(-2, 'days'),
        distanceMeters: 6000,
        timeSeconds: 36 * 60,
      },
      {
        id: '2',
        date: moment().add(-4, 'days'),
        distanceMeters: 4000,
        timeSeconds: 26 * 60,
      },
      {
        id: '3',
        date: moment().add(-6, 'days'),
        distanceMeters: 8000,
        timeSeconds: 46 * 60,
      },
    ]}
  />
</div>;

/* global document */
ReactDOM.render(
  <MuiThemeProvider muiTheme={getMuiTheme()}>
    <AppTemplate />
  </MuiThemeProvider>,
  document.getElementById('app')
);
