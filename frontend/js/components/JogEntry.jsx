import Moment from 'react-moment';
import moment from 'moment';
import React from 'react';
import { Card, CardActions, CardHeader, CardText } from 'material-ui/Card';
import { Icon } from 'react-fa';
import { colors } from 'material-ui/styles';
import IconButton from 'material-ui/IconButton';

import MomentProperty from 'joggr/components/MomentProperty';

const CardIcon = ({ name, color }) => (
  <Icon name={name} style={{ color, paddingRight: '1em' }} />
);
CardIcon.propTypes = {
  name: React.PropTypes.string,
  color: React.PropTypes.string,
};

const JogEntry = ({ activity, onRemove }) => <Card
  expandable
  initiallyExpanded={false}
  style={{
    margin: '2ex',
    position: 'relative',
    backgroundColor: activity.date.isBefore(moment().add(10, 'seconds')) ?
    'inherit' :
    colors.gray200,
  }}
>
  <CardHeader
    title={
      <span style={{ fontSize: '16pt' }}>
        <CardIcon name="calendar" color={colors.green700} />
        <Moment date={activity.date} fromNow ago /> ago
      </span>
    }
  />
  <CardText>
    <div style={{ fontSize: '14pt' }}>
      <div>
        <CardIcon name="map" color={colors.tealA700} />
        {activity.distanceMeters} m
      </div>
      <div>
        <CardIcon name="clock-o" color={colors.orange700} />
        {(activity.timeSeconds / 60).toFixed()}m&nbsp;
        {activity.timeSeconds - (((activity.timeSeconds / 60).toFixed()) * 60)}s
      </div>
      <div>
        <CardIcon name="rocket" color={colors.deepOrange700} />
        {activity.speedKMpH().toFixed(2)} km/h
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
        onTouchTap={() => onRemove(activity)}
      />
    </div>
  </CardActions>
</Card>;

JogEntry.propTypes = {
  activity: React.PropTypes.shape({
    id: React.PropTypes.string,
    date: MomentProperty,
    distanceMeters: React.PropTypes.number,
    timeSeconds: React.PropTypes.number,
    speedKMpH: React.PropTypes.func,
  }),
  onRemove: React.PropTypes.func,
};

export default JogEntry;
