import { createStore } from 'redux';
import persistState from 'redux-localstorage';
import { composeWithDevTools } from 'redux-devtools-extension';

import { JogEntry, JogEntryList, JogEntryViewOptions, Credentials } from 'joggr/model';

const PUSH_JOG_ENTRY = 'PUSH_JOG_ENTRY';
const REMOVE_JOG_ENTRY = 'REMOVE_JOG_ENTRY';
const TOGGLE_WEEKLY_REPORT = 'TOGGLE_WEEKLY_REPORT';
const UPDATE_FILTERS = 'UPDATE_FILTERS';
const LOGIN_OR_REGISTER = 'LOGIN_OR_REGISTER';

export function pushJogEntry(entry) {
  return {
    type: PUSH_JOG_ENTRY,
    entry: new JogEntry(entry),
  };
}

export function removeJogEntry(entry) {
  return {
    type: REMOVE_JOG_ENTRY,
    entry,
  };
}

export function toggleWeeklyReport() {
  return {
    type: TOGGLE_WEEKLY_REPORT,
  };
}

export function updateFilters(filterDateFrom, filterDateTo) {
  return {
    type: UPDATE_FILTERS,
    filterDateFrom,
    filterDateTo,
  };
}

export function login({ username, password }) {
  return {
    type: LOGIN_OR_REGISTER,
    new_user: false,
    username,
    password,
  };
}

export function logout() {
  return {
    type: LOGIN_OR_REGISTER,
    new_user: false,
    username: '',
    password: '',
  };
}

export function regsiter({ username, password }) {
  return {
    type: LOGIN_OR_REGISTER,
    new_user: true,
    username,
    password,
  };
}

const defaultState = {
  jogEntries: new JogEntryList(),
  options: new JogEntryViewOptions(),
  credentials: new Credentials(),
};

function reducer(state, action) {
  switch (action.type) {
    case PUSH_JOG_ENTRY:
      return Object.assign({}, state, {
        jogEntries: state.jogEntries.addOrReplace(action.entry),
      });
    case REMOVE_JOG_ENTRY:
      return Object.assign({}, state, {
        jogEntries: state.jogEntries.remove(action.entry),
      });
    case TOGGLE_WEEKLY_REPORT:
      return Object.assign({}, state, {
        options: state.options.withToggledShow(),
      });
    case UPDATE_FILTERS: {
      const updatedOptions = state.options.withFilters(
        action.filterDateFrom, action.filterDateTo
      );
      return Object.assign({}, state, {
        options: updatedOptions,
        jogEntries: state.jogEntries.makrDateVisibility(updatedOptions),
      });
    }
    case LOGIN_OR_REGISTER: {
      return Object.assign({}, state, {
        credentials: new Credentials(action),
      });
    }
    default: {
      if (!state) {
        return defaultState;
      }
      if (
        state.jogEntries instanceof JogEntryList &&
        state.options instanceof JogEntryViewOptions
      ) {
        return state;
      }
      const resultingState = Object.assign({}, state);
      if (state.options) {
        Object.assign(resultingState, {
          options: new JogEntryViewOptions(state.options),
        });
      }
      if (state.jogEntries) {
        Object.assign(resultingState, {
          jogEntries: JogEntryList.fromJS(state.jogEntries).makrDateVisibility(state.options),
        });
      }
      return resultingState;
    }
  }
}

export const store = createStore(
  reducer,
  defaultState,
  composeWithDevTools(
    persistState()
  )
);
