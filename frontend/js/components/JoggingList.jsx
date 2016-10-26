import React from 'react';
import moment from 'moment';
import Paper from 'material-ui/Paper';
import { colors } from 'material-ui/styles';
import IconButton from 'material-ui/IconButton';
import DatePicker from 'material-ui/DatePicker';
import { Toolbar, ToolbarGroup, ToolbarSeparator } from 'material-ui/Toolbar';

import AddEditDialog from 'joggr/components/AddEditDialog';
import JogEntry from 'joggr/components/JogEntry';

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
  {activities.map(activity =>
    <JogEntry {...activity} key={activity.id} />
  )}
</Paper>;

JoggingList.propTypes = {
  activities: React.PropTypes.arrayOf(React.PropTypes.object).isRequired,
};

export default JoggingList;
