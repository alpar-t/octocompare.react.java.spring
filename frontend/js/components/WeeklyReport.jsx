import React from 'react';
import { LineChart, Line, XAxis, YAxis, Tooltip } from 'recharts';
import SmoothCollapse from 'react-smooth-collapse';
import { colors } from 'material-ui/styles';

const Chart = ({ title, data, ylabel, color }) => <div>
  <h5> {title} </h5>
  <LineChart
    width={800}
    height={200}
    data={data}
    margin={{ top: 40, right: 90, left: 30, bottom: 40 }}
  >
    <Tooltip />
    <XAxis dataKey="week" label="week of year" />
    <YAxis label={ylabel} />
    <Line type="monotone" dataKey={ylabel} stroke={color} />
  </LineChart>
</div>;
Chart.propTypes = {
  title: React.PropTypes.string,
  data: React.PropTypes.arrayOf(
    React.PropTypes.shape({
      /* eslint react/no-unused-prop-types: 0*/
      week: React.PropTypes.number,
    })),
  ylabel: React.PropTypes.string,
  color: React.PropTypes.string,
};

const WeeklyReport = ({ avgSpeed, avgDistance, expanded }) => <SmoothCollapse
  expanded={expanded}
>
  <Chart
    title="Weekly average speed"
    ylabel="speed"
    data={avgSpeed}
    color={colors.deepPurpleA200}
  />
  <Chart
    title="Weekly average distance"
    ylabel="distance"
    data={avgDistance}
    color={colors.orange500}
  />
</SmoothCollapse>;

WeeklyReport.propTypes = {
  avgSpeed: React.PropTypes.arrayOf(
    React.PropTypes.shape({
      /* eslint react/no-unused-prop-types: 0*/
      week: React.PropTypes.number,
      speed: React.PropTypes.number,
    })
  ),
  expanded: React.PropTypes.bool,
  avgDistance: React.PropTypes.arrayOf(
    React.PropTypes.shape({
      /* eslint react/no-unused-prop-types: 0*/
      week: React.PropTypes.number,
      distance: React.PropTypes.number,
    })
  ),
};

export default WeeklyReport;
