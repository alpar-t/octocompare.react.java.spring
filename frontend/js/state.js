import { createStore } from 'redux';
import persistState from 'redux-localstorage';
import { composeWithDevTools } from 'redux-devtools-extension';

import { JogEntry, JogEntryList, JogEntryViewOptions } from 'joggr/model';

const PUSH_JOG_ENTRY = 'PUSH_JOG_ENTRY';
const REMOVE_JOG_ENTRY = 'REMOVE_JOG_ENTRY';

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

const defaultState = {
  jogEntries: new JogEntryList(),
  options: new JogEntryViewOptions(),
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
    default:
      if (state.jogEntries instanceof JogEntryList) {
        return state;
      }
      if (!state) {
        return defaultState;
      }
      if (state.jogEntries) {
        return Object.assign({}, state, {
          jogEntries: JogEntryList.fromJS(state.jogEntries),
          options: new JogEntryViewOptions(state.optionss),
        });
      }
      return state;
  }
}

export const store = createStore(
  reducer,
  defaultState,
  composeWithDevTools(
    persistState()
  )
);
