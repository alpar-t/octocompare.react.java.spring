import uuid from 'node-uuid';
import moment from 'moment';
import * as Immutable from 'immutable';
/* eslint new-cap: 0*/

const MPH_TO_KMPH = 3.6;

export class JogEntry extends Immutable.Record({
  id: '',
  date: moment(),
  timeSeconds: 1,
  distanceMeters: 1,
  visible: true,
}) {
  constructor(props) {
    if (props) {
      super(Object.assign({}, props,
        {
          id: props.id ? props.id : uuid.v4(),
          date: moment(props.date),
          distanceMeters: parseInt(props.distanceMeters, 10),
          timeSeconds: parseInt(props.timeSeconds, 10),
        }
      ));
    } else {
      super({ id: uuid.v4() });
    }
  }

  speedKMpH() {
    return ((this.distanceMeters / this.timeSeconds) * MPH_TO_KMPH);
  }
}

export class JogEntryList extends Immutable.Record({
  delegate: [],
}) {
  static fromJS(js) {
    return new JogEntryList(
      js.delegate.map(it => new JogEntry(it))
    );
  }

  constructor(seed) {
    super({ delegate: seed instanceof Immutable.List ? seed : Immutable.List(seed) });
  }

  addOrReplace(entry) {
    return new JogEntryList(this.delegate.push(entry));
  }

  remove(entry) {
    return new JogEntryList(this.delegate.filter(it => it.id !== entry.id));
  }

  all() {
    return this.delegate.filter(it => it.visible);
  }

  orderByDate() {
    return new JogEntryList(
        this.delegate.sort((vala, valb) =>
          !vala.date.isBefore(valb.date)
        )
    );
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
  filterDateFrom: moment().add(-1, 'months'),
  filterDateTo: moment(),
}) {
  constructor(props) {
    if (props) {
      super(Object.assign({}, props,
        {
          filterDateTo: moment(props.filterDateTo),
          filterDateFrom: moment(props.filterDateFrom),
        }
      ));
    } else {
      super();
    }
  }

  withToggledShow() {
    return new JogEntryViewOptions(Object.assign(
      {}, this.toJS(), { showReport: !this.showReport }
    ));
  }
}
