import { createStore } from 'redux';
import { persistStore, autoRehydrate } from 'redux-persist';
import { REHYDRATE } from 'redux-persist/constants';
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

export const POST_MESSAGE = 'POST_MESSAGE';
export function postMessage(message = '') {
  return {
    type: POST_MESSAGE,
    message,
  };
}

const defaultState = {
  allJogEntries: {},
  jogEntries: new JogEntryList(),
  options: new JogEntryViewOptions(),
  credentials: new Credentials(),
  message: '',
};

function syncedJogEntries(reducerFunc) {
  return (state, action) => {
    const reduced = Object.assign({}, reducerFunc(state, action));
    reduced.allJogEntries = Object.assign(
      {}, reduced.allJogEntries, { [reduced.credentials.username]: reduced.jogEntries }
    );
    return reduced;
  };
}

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
    case POST_MESSAGE:
      return Object.assign({}, state, {
        message: action.message,
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
      const credentials = new Credentials(action);
      const jogEntries = JogEntryList.fromJS(
        state.allJogEntries[credentials.username]
      ).makrDateVisibility(state.options);
      return Object.assign({}, state, {
        credentials,
        jogEntries,
      });
    }
    case REHYDRATE: {
      const incoming = action.payload;
      const options = new JogEntryViewOptions(incoming.options);
      const credentials = new Credentials(incoming.credentials);
      const allJogEntries = incoming.allJogEntries;
      const jogEntries = JogEntryList.fromJS(
        allJogEntries ? allJogEntries[credentials.username] : {}
      ).makrDateVisibility(options);
      if (incoming) {
        return {
          jogEntries,
          options,
          credentials,
          allJogEntries,
        };
      }
      return state;
    }
    default: {
      if (!state) {
        return defaultState;
      }
      return state;
    }
  }
}

export const store = createStore(
  syncedJogEntries(reducer),
  defaultState,
  composeWithDevTools(
    autoRehydrate()
  )
);
persistStore(
  store,
  {
    blacklist: ['jogEntries'],
  },
  () => {
    store.dispatch(toggleWeeklyReport());
  }
);

store.subscribe(() => {
  const state = store.getState();
  // only take the first one, when we dispatch the result, next one will pick up
  const entryToSave = state.jogEntries.pendingSave().first();
  if (entryToSave) {
    const combinedCredentials = `${state.credentials.username}:${state.credentials.password}`;
    /* global window */
    window.fetch(
      `jogEntries/${entryToSave.id}`,
      {
        headers: {
          Accept: 'application/json',
          'Content-Type': 'application/json',
          Authorization: `Basic ${new Buffer(combinedCredentials).toString('base64')}`,
        },
        body: JSON.stringify(entryToSave.toJS()),
        method: 'put',
        credentials: 'omit',
      }
    ).then((response) => {
      if (response.ok) {
        store.dispatch(pushJogEntry(entryToSave.toJS()));
      }
      return response.json();
    })
    .then((json) => {
      if (json.error && !state.message) {
        store.dispatch(postMessage(`Failed to update jog enty: ${json.message}`));
      }
    });
  }
});
