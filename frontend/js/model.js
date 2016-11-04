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
  markedForRemove: false,
  knownByServer: false,
  removedOnServer: false,
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

  withAdjustedVisibility(filterDateFrom, filterDateTo) {
    return new JogEntry(Object.assign(this.toJS(), {
      visible: this.date.isBefore(
        moment(filterDateTo).add(1, 'days')
      ) && this.date.isAfter(
        filterDateFrom
      ) &&
        !this.markedForRemove,
    }));
  }

  withMarkForRemove() {
    return new JogEntry(Object.assign(this.toJS(), {
      visible: false,
      markedForRemove: true,
    }));
  }

  withKnownByServer() {
    return new JogEntry(Object.assign(this.toJS(), {
      knownByServer: true,
    }));
  }

  withRemovedOnServer() {
    return new JogEntry(Object.assign(this.toJS(), {
      knownByServer: false,
      removedOnServer: true,
      visible: false,
      markedForRemove: true,
    }));
  }
}

export class JogEntryList extends Immutable.Record({
  delegate: [],
  username: '',
}) {
  static fromJS(js) {
    if (js && js.delegate) {
      return new JogEntryList(
        js.delegate.map(it => new JogEntry(it))
      );
    }
    return new JogEntryList();
  }

  constructor(seed, username = '') {
    super({
      delegate: seed instanceof Immutable.List ? seed : Immutable.List(seed),
      username,
    });
  }

  addOrReplace(entry) {
    return new JogEntryList(
      this.eject(entry).delegate.push(entry)
    );
  }

  eject(entry) {
    return new JogEntryList(this.delegate.filter(it => it.id !== entry.id));
  }

  remove(entry) {
    return this.addOrReplace(entry.withMarkForRemove());
  }

  makrDateVisibility(options) {
    return new JogEntryList(
      this.delegate.map(it =>
        it.withAdjustedVisibility(options.filterDateFrom, options.filterDateTo)
      )
    );
  }

  pendingRemoval() {
    return new JogEntryList(
      this.delegate.filter(it =>
        !it.removedOnServer && it.markedForRemove
      )
      .map(it => it.withRemovedOnServer())
    );
  }

  pendingSave() {
    return new JogEntryList(
      this.delegate.filter(it =>
        !it.knownByServer && !it.removedOnServer && !it.markedForRemove
      )
      .map(it => it.withKnownByServer())
    );
  }

  all() {
    return this.delegate.filter(it => it.visible);
  }

  reportSpeedPerWeek() {
    const weekAndSpeed = this.all()
    .map(entry =>
        ({ week: entry.date.week(), speed: entry.speedKMpH() })
    )
    .reduce((collect, value) => {
      const ret = Object.assign({}, collect);
      if (ret[value.week]) {
        ret[value.week].speed += value.speed;
        ret[value.week].count += 1.0;
      } else {
        ret[value.week] = { speed: value.speed, count: 1.0 };
      }
      return ret;
    }, {});

    return Object.keys(weekAndSpeed).map(week => ({
      week: parseInt(week, 10),
      speed: weekAndSpeed[week].speed / weekAndSpeed[week].count,
    }));
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
          filterDateFrom: props.filterDateFrom ? moment(props.filterDateFrom) : moment().add(-1, 'months'),
          filterDateTo: props.filterDateTo ? moment(props.filterDateTo) : moment(),
        }
      ));
    } else {
      super();
    }
  }

  withFilters(filterDateFrom, filterDateTo) {
    const update = {};
    if (filterDateFrom) {
      Object.assign(update, { filterDateFrom });
    }
    if (filterDateTo) {
      Object.assign(update, { filterDateTo });
    }
    return new JogEntryViewOptions(
      Object.assign({}, this.toJS(), update)
    );
  }

  withToggledShow() {
    return new JogEntryViewOptions(Object.assign(
      {}, this.toJS(), { showReport: !this.showReport }
    ));
  }
}

export class Credentials extends Immutable.Record({
  username: '',
  password: '',
}) {

}
