import React from 'react';
import moment from 'moment';
import { colors } from 'material-ui/styles';
import IconButton from 'material-ui/IconButton';
import DatePicker from 'material-ui/DatePicker';
import { Toolbar as MaterialToolbar, ToolbarGroup, ToolbarSeparator } from 'material-ui/Toolbar';

import AddEditDialog from 'joggr/components/AddEditDialog';

const Toolbar = ({
    showReport,
    filterDateTo,
    filterDateFrom,
    onAddOrEdit,
    onTogleWeeklyReport,
    onDateFiltersUpdate,
}) => <div style={{ fontSize: '18pt' }}>
  <MaterialToolbar>
    <ToolbarGroup >
      <AddEditDialog onAddOrEdit={onAddOrEdit} />
      <IconButton
        iconClassName="fa fa-bar-chart"
        iconStyle={{ color: showReport ? colors.orange700 : colors.orange200 }}
        onTouchTap={onTogleWeeklyReport}
      />
      <ToolbarSeparator />
      <DatePicker
        style={{ marginLeft: '2em' }}
        hintText="filter from date ..."
        container="inline"
        autoOk
        defaultDate={filterDateFrom.toDate()}
        maxDate={filterDateTo.toDate()}
        onChange={(event, value) => { onDateFiltersUpdate(value, null); }}
      />
      <DatePicker
        style={{ marginLeft: '1em' }}
        hintText="filter to date ..."
        container="inline"
        autoOk
        defaultDate={filterDateTo.toDate()}
        minDate={filterDateFrom.toDate()}
        onChange={(event, value) => { onDateFiltersUpdate(null, value); }}
      />
    </ToolbarGroup>
  </MaterialToolbar>
</div>;

Toolbar.propTypes = {
  showReport: React.PropTypes.bool,
  filterDateTo: React.PropTypes.instanceOf(moment),
  filterDateFrom: React.PropTypes.instanceOf(moment),
  onAddOrEdit: React.PropTypes.func,
  onTogleWeeklyReport: React.PropTypes.func,
  onDateFiltersUpdate: React.PropTypes.func,
};

export default Toolbar;
