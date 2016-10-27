import { createStore } from 'redux';
import persistState from 'redux-localstorage';
import { composeWithDevTools } from 'redux-devtools-extension';

import { JogEntry, JogEntryList, JogEntryViewOptions } from 'joggr/model';

const PUSH_JOG_ENTRY = 'PUSH_JOG_ENTRY';
const REMOVE_JOG_ENTRY = 'REMOVE_JOG_ENTRY';
const TOGGLE_WEEKLY_REPORT = 'TOGGLE_WEEKLY_REPORT';
const UPDATE_FILTERS = 'UPDATE_FILTERS';

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
    default:
      if (state.jogEntries instanceof JogEntryList) {
        return state;
      }
      if (!state) {
        return defaultState;
      }
      if (state.jogEntries) {
        const options = new JogEntryViewOptions(state.options);
        return Object.assign({}, state, {
          jogEntries: JogEntryList.fromJS(state.jogEntries).makrDateVisibility(options),
          options,
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
