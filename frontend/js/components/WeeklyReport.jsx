import React from 'react';
import { LineChart, Line, XAxis, YAxis, Tooltip } from 'recharts';
import SmoothCollapse from 'react-smooth-collapse';

const WeeklyReport = ({ data, expanded }) => <SmoothCollapse
  expanded={expanded}
>
  <h5 style={{ textAlign: 'center' }}> Weekly average speed</h5>
  <LineChart
    width={800}
    height={200}
    data={data}
    margin={{ top: 40, right: 90, left: 30, bottom: 40 }}
  >
    <Tooltip />
    <XAxis dataKey="week" label="week of year" />
    <YAxis label="speed" />
    <Line type="monotone" dataKey="speed" stroke="#82ca9d" />
  </LineChart>
</SmoothCollapse>;

WeeklyReport.propTypes = {
  data: React.PropTypes.arrayOf(
    React.PropTypes.shape({
      /* eslint react/no-unused-prop-types: 0*/
      week: React.PropTypes.number,
      speed: React.PropTypes.number,
    })
  ),
  expanded: React.PropTypes.bool,
};

export default WeeklyReport;
