import Moment from 'react-moment';
import moment from 'moment';
import React from 'react';
import { Card, CardActions, CardHeader, CardText } from 'material-ui/Card';
import { Icon } from 'react-fa';
import { colors } from 'material-ui/styles';
import IconButton from 'material-ui/IconButton';

const MPH_TO_KMPH = 3.6;

const CardIcon = ({ name, color }) => (
  <Icon name={name} style={{ color, paddingRight: '1em' }} />
);
CardIcon.propTypes = {
  name: React.PropTypes.string,
  color: React.PropTypes.string,
};

const JogEntry = ({ id, date, distanceMeters, timeSeconds }) =>
  <Card
    expandable
    initiallyExpanded={false}
    style={{ margin: '2ex', position: 'relative' }}
  >
    <CardHeader
      title={
        <span style={{ fontSize: '16pt' }}>
          <CardIcon name="calendar" color={colors.green700} />
          <Moment date={date} fromNow ago /> ago
        </span>
      }
    />
    <CardText>
      <div style={{ fontSize: '14pt' }}>
        <div>
          <CardIcon name="map" color={colors.tealA700} />
          {distanceMeters} m
        </div>
        <div>
          <CardIcon name="clock-o" color={colors.orange700} />
          {(timeSeconds / 60).toFixed()}m&nbsp;
          {timeSeconds - (((timeSeconds / 60).toFixed()) * 60)}s
        </div>
        <div>
          <CardIcon name="rocket" color={colors.deepOrange700} />
          {((distanceMeters / timeSeconds) * MPH_TO_KMPH).toFixed(2)} km/h
        </div>
      </div>
    </CardText>
    <CardActions style={{ position: 'absolute', top: 0, right: 0 }}>
      <div style={{ fontSize: '18pt' }}>
        <IconButton
          iconClassName="fa fa-pencil"
          iconStyle={{ color: colors.yellow700 }}
        />
        <IconButton
          iconClassName="fa fa-remove"
          iconStyle={{ color: colors.red900 }}
        />
      </div>
    </CardActions>
  </Card>;
JogEntry.propTypes = {
  id: React.PropTypes.string.isRequired,
  distanceMeters: React.PropTypes.number.isRequired,
  date: it => moment(it).isValid(),
  timeSeconds: React.PropTypes.number.isRequired,
};

export default JogEntry;
