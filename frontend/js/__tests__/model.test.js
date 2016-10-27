import { JogEntry, JogEntryList, JogEntryViewOptions } from 'joggr/model';
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
  });

  it('Custom values', () => {
    const entry = new JogEntry({
      visible: false,
      distanceMeters: 20,
      timeSeconds: 30,
      date: someTime,
    });
    expect(entry.visible).toEqual(false);
    expect(entry.distanceMeters).toEqual(20);
    expect(entry.timeSeconds).toEqual(30);
    expect(entry.date).toEqual(someTime);
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

describe('AuctionList', () => {
  const empty = new JogEntryList();
  const al = new JogEntryList([
    validEntry,
    new JogEntry({ date: someTime }),
    new JogEntry({ visible: false }),
  ]);

  it('Construct empty', () => {
    expect(empty.all().size).toEqual(0);
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
});
