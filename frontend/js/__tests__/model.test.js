import { JogEntry, JogEntryList, JogEntryViewOptions } from 'octocompare/model';
import moment from 'moment';

const validEntry = new JogEntry();
const someTime = moment('2010-08-22');
const someOtherTime = moment('1987-08-25');

describe('JogEntry', () => {
  it('Construct JogEntry with defaults', () => {
    expect(validEntry.id.length).toEqual(36);
    expect(validEntry.id).toContain('-4'); // uuid v4 spec
  });

  it('Expect it to be immutable', () => {
    expect(() => { validEntry.id = 1; })
      .toThrowError('Cannot set on an immutable record.');
  });

  it('Custom IDs preserverd', () => {
    const entry = new JogEntry({ id: 1 });
    expect(entry.id).toEqual(1);
  });

  it('Default values', () => {
    const entry = new JogEntry();
    expect(entry.visible).toEqual(true);
    expect(entry.distanceMeters).toEqual(1);
    expect(entry.timeSeconds).toEqual(1);
    expect(entry.knownByServer).toEqual(false);
    expect(entry.markedForRemove).toEqual(false);
    expect(entry.removedOnServer).toEqual(false);
  });

  it('Known by server', () => {
    const entry = new JogEntry().withKnownByServer();
    expect(entry.knownByServer).toEqual(true);
    expect(entry.markedForRemove).toEqual(false);
    expect(entry.removedOnServer).toEqual(false);
    expect(entry.visible).toEqual(true);
  });

  it('Marked for remove', () => {
    const entry = new JogEntry().withKnownByServer().withMarkForRemove();
    expect(entry.knownByServer).toEqual(true);
    expect(entry.markedForRemove).toEqual(true);
    expect(entry.removedOnServer).toEqual(false);
    expect(entry.visible).toEqual(false);
  });

  it('remove from server', () => {
    const entry = new JogEntry().withKnownByServer().withRemovedOnServer();
    expect(entry.knownByServer).toEqual(false);
    expect(entry.markedForRemove).toEqual(true);
    expect(entry.removedOnServer).toEqual(true);
    expect(entry.visible).toEqual(false);
  });

  it('Custom values', () => {
    const entry = new JogEntry({
      visible: false,
      distanceMeters: 20,
      timeSeconds: 30,
      date: someTime,
      knownByServer: true,
    });
    expect(entry.visible).toEqual(false);
    expect(entry.knownByServer).toEqual(true);
    expect(entry.distanceMeters).toEqual(20);
    expect(entry.timeSeconds).toEqual(30);
    expect(entry.date).toEqual(someTime);
  });

  it('adjust visibility', () => {
    const now = moment();
    const entry = new JogEntry({ date: now });
    expect(entry.visible).toEqual(true);
    expect(
      entry
        .withAdjustedVisibility(moment(now).add(-2, 'days'), moment(now).add(-1, 'days'))
        .visible
    ).toEqual(false);

    expect(
      entry
        .withAdjustedVisibility(moment(now).add(1, 'days'), moment(now).add(2, 'days'))
        .visible
    ).toEqual(false);

    expect(
      entry
        .withAdjustedVisibility(moment(now).add(-1, 'days'), moment(now).add(1, 'days'))
        .visible
    ).toEqual(true);
  });

  it('speedKMpH', () => {
    const entry = new JogEntry({ distanceMeters: 6000, timeSeconds: 331 });
    expect(entry.speedKMpH()).toEqual(65.25679758308158);
  });
});

describe('JogEntryViewOptions', () => {
  it('with defaults', () => {
    const options = new JogEntryViewOptions();
    expect(options.showReport).toEqual(false);
  });

  it('with custom values', () => {
    const options = new JogEntryViewOptions({
      showReport: true,
      filterDateTo: someTime,
      filterDateFrom: someOtherTime,
    });
    expect(options.showReport).toEqual(true);
    expect(options.filterDateTo).toEqual(someTime);
    expect(options.filterDateFrom).toEqual(someOtherTime);
  });

  it('with toggled show', () => {
    const options = new JogEntryViewOptions({ showReport: true });
    expect(options.showReport).toEqual(true);
    expect(options.withToggledShow().showReport).toEqual(false);
    expect(() => { options.withToggledShow().showReport = false; })
      .toThrowError('Cannot set on an immutable record.');
  });

  it('Expect it to be immutable', () => {
    const options = new JogEntryViewOptions({ showReport: true });
    expect(() => { options.showReport = false; })
      .toThrowError('Cannot set on an immutable record.');
  });
});

describe('JogEntryList', () => {
  const empty = new JogEntryList();
  const al = new JogEntryList([
    validEntry,
    new JogEntry({ date: someTime }),
    new JogEntry({ visible: false }),
  ]);

  it('Construct empty', () => {
    expect(empty.all().size).toEqual(0);
  });

  it('pendingSave', () => {
    const entry = new JogEntry();
    const testee = new JogEntryList([entry]);
    expect(testee.pendingSave().first().knownByServer).toEqual(true);
    const allKnown = testee.addOrReplace(testee.pendingSave().first());
    expect(allKnown.pendingSave().delegate.size).toEqual(0);
  });

  it('Construct non empty', () => {
    expect(al.all().size).toEqual(2);
  });

  it('Immutable', () => {
    expect(empty.all().size).toEqual(0);
    empty.all().push(1);
    expect(empty.all().size).toEqual(0);
  });

  it('can add', () => {
    const someEntry = new JogEntry();
    expect(al.addOrReplace(someEntry).all().size).toEqual(3);
  });

  it('report speed per week', () => {
    const list = new JogEntryList([
      new JogEntry({ distanceMeters: 1000, timeSeconds: 180, date: someTime }),
      new JogEntry({ distanceMeters: 3000, timeSeconds: 600, date: someTime }),
      new JogEntry({ distanceMeters: 2000, timeSeconds: 180, date: moment(someTime).add(8, 'days') }),
      new JogEntry({ distanceMeters: 6000, timeSeconds: 600, date: moment(someTime).add(8, 'days') }),
      new JogEntry({ distanceMeters: 6000, timeSeconds: 600, date: moment(someTime).add(15, 'days') }),
    ]);
    expect(list.reportSpeedPerWeek()).toEqual([
        { week: 35, speed: 19 },
        { week: 36, speed: 38 },
        { week: 37, speed: 36 },
    ]);
  });
});
