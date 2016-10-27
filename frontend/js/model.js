import uuid from 'node-uuid';
import moment from 'moment';
import * as Immutable from 'immutable';
/* eslint new-cap: 0*/

export class JogEntry extends Immutable.Record({
  id: '',
  date: moment(),
  timeSeconds: 1,
  distanceMeters: 1,
  visible: true,
}) {
  constructor(props = {}) {
    super(Object.assign({}, props,
      {
        id: uuid.v4(),
        date: moment(props.date),
      }
    ));
  }
}

export class JogEntryList extends Immutable.Record({
  delegate: [],
}) {
  static fromJS(js) {
    return new JogEntryList(
      js.delegate.map(a => new JogEntry(a))
    );
  }

  constructor(seed) {
    super({ delegate: seed instanceof Immutable.List ? seed : Immutable.List(seed) });
  }

  add(entry) {
    return new JogEntryList(this.delegate.push(entry));
  }

  all() {
    return this.delegate.filter(it => it.visible);
  }

  reverse() {
    return this.all().reverse();
  }

  first() {
    return this.all().get(0);
  }

  last() {
    return this.all().get(-1);
  }
}

export class JogEntryViewOptions extends Immutable.Record({
  showReport: false,
  filterDateFrom: moment().add(-30, 'days'),
  filterDateTo: moment(),
}) {
  constructor(props = {}) {
    super(Object.assign({}, props,
      {
        filterDateTo: moment(props.filterDateTo),
        filterDateFrom: moment(props.filterDateFrom),
      }
    ));
  }

  withToggledShow() {
    return new JogEntryViewOptions(Object.assign(
      {}, this.toJS(), { showReport: !this.showReport }
    ));
  }
}
