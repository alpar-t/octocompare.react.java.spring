import React from 'react';
import moment from 'moment';
import Dialog from 'material-ui/Dialog';
import FlatButton from 'material-ui/FlatButton';
import IconButton from 'material-ui/IconButton';
import { colors } from 'material-ui/styles';
import Formsy from 'formsy-react';
import FormsyText from 'formsy-material-ui/lib/FormsyText';
import FormsyDate from 'formsy-material-ui/lib/FormsyDate';

import JogEntry from 'joggr/components/JogEntry';

export default class AddEditDialog extends React.Component {

  constructor(props) {
    super(props);

    this.state = {
      open: false,
      valid: false,
    };

    this.handleOpen = this.handleOpen.bind(this);
    this.handleClose = this.handleClose.bind(this);
    this.handleSubmit = this.handleSubmit.bind(this);
    this.handleChange = this.handleChange.bind(this);
  }

  setValid(isValid) {
    this.setState({ valid: isValid });
  }

  handleChange(model) {
    this.setState({ model });
  }

  handleSubmit() {
    if (this.state.valid) {
      this.props.onAddOrEdit(this.state.model);
      this.handleClose();
    }
  }

  handleClose() {
    this.setState({ open: false });
  }

  handleOpen() {
    this.setState({ open: true });
  }

  render() {
    const actions = [
      <FlatButton
        label="Cancel"
        onTouchTap={this.handleClose}
      />,
      <FlatButton
        label="Submit"
        primary
        onTouchTap={this.handleSubmit}
      />,
    ];

    return (
      <span>
        <IconButton
          iconClassName="fa fa-plus"
          iconStyle={{ color: colors.blue700 }}
          onTouchTap={this.handleOpen}
        />
        <Dialog
          title={`${this.props.activity ? 'Edit' : 'Add new'} activity`}
          actions={actions}
          modal={false}
          open={this.state.open}
          onRequestClose={this.handleClose}
        >
          <Formsy.Form
            onValidSubmit={this.handleSubmit}
            onChange={this.handleChange}
            onValid={() => this.setValid(true)}
            onInvalid={() => this.setValid(false)}
          >
            <FormsyDate
              floatingLabelText="When did you jog?"
              name="date"
              defaultDate={moment().toDate()}
              autoOk
            />
            <FormsyText
              hintText="how far did you go (meters))?"
              name="distanceMeters"
              type="number"
              required
              updateImmediately
            /> <br />
            <FormsyText
              hintText="how far did you go (seconds)?"
              name="timeSeconds"
              type="number"
              required
              updateImmediately
            />
          </Formsy.Form>
        </Dialog>
      </span>
    );
  }
}
AddEditDialog.propTypes = {
  activity: React.PropTypes.shape(JogEntry.propTypes),
  onAddOrEdit: React.PropTypes.func,
};
