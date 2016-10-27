import moment from 'moment';

export default (props, propName, componentName) => {
  if (!moment(props[propName]).isValid()) {
    return new Error(`${componentName} got an unexpected ` +
              `${propName} = ${props[propName]}. expected to get a Moment instance.`);
  }
  return null;
};
