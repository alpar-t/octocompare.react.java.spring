import React from 'react';
import moment from 'moment';
import Paper from 'material-ui/Paper';
import { colors } from 'material-ui/styles';
import IconButton from 'material-ui/IconButton';
import DatePicker from 'material-ui/DatePicker';
import { Toolbar, ToolbarGroup, ToolbarSeparator } from 'material-ui/Toolbar';

import AddEditDialog from 'joggr/components/AddEditDialog';
import JogEntry from 'joggr/components/JogEntry';

import { LineChart, Line, XAxis, YAxis, Tooltip } from 'recharts';

const data = [
      { week: 1, speed: 4.4 },
      { week: 2, speed: 9.1 },
      { week: 3, speed: 11.3 },
];

const JoggingList = ({ activities }) => <Paper
  style={{ padding: '2ex', margin: '1ex' }}
>
  <h1 style={{ color: colors.grey700 }}>
    Logged activities
  </h1>
  <div style={{ fontSize: '18pt' }}>
    <Toolbar>
      <ToolbarGroup >
        <AddEditDialog />
        <IconButton
          iconClassName="fa fa-bar-chart"
          iconStyle={{ color: colors.orange700 }}
        />
        <ToolbarSeparator />
        <DatePicker
          style={{ marginLeft: '2em' }}
          hintText="filter from date ..."
          container="inline"
          autoOk
          defaultDate={moment().add(-30, 'days').toDate()}
        />
        <DatePicker
          style={{ marginLeft: '1em' }}
          hintText="filter to date ..."
          container="inline"
          autoOk
          defaultDate={moment().toDate()}
        />
      </ToolbarGroup>
    </Toolbar>
  </div>

  <Paper style={{ marginTop: '2ex', padding: 20 }}>
    <LineChart
      width={900}
      height={200}
      data={data}
      margin={{ top: 30, right: 80, left: 30, bottom: 30 }}
    >
      <Tooltip />
      <XAxis dataKey="week" label="week of year" />
      <YAxis label="speed" />
      <Line type="monotone" dataKey="speed" stroke="#82ca9d" />
    </LineChart>
  </Paper>

  {activities.map(activity =>
    <JogEntry {...activity} key={activity.id} />
  )}
</Paper>;

JoggingList.propTypes = {
  activities: React.PropTypes.arrayOf(React.PropTypes.object).isRequired,
};

export default JoggingList;
