import React from 'react';
import Snackbar from 'material-ui/Snackbar';

import { postMessage } from 'joggr/state';

const MessageDrivenSnackbar = ({ message, dispatch }) => <Snackbar
  open={Boolean(message)}
  message={message || 'no message'}
  onRequestClose={() => dispatch(postMessage(''))}
/>;
MessageDrivenSnackbar.propTypes = {
  message: React.PropTypes.string,
  dispatch: React.PropTypes.func,
};

export default MessageDrivenSnackbar;
