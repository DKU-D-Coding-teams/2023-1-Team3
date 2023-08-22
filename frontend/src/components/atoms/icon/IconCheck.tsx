export default function IconCheck({
  size = '1rem',
  color = 'black',
  ...props
}) {
  return (
    <svg
      xmlns="http://www.w3.org/2000/svg"
      width={size}
      height={size}
      viewBox="0 0 24 24"
      style={{
        fill: color,
      }}
      {...props}
    >
      <path d="m10 15.586-3.293-3.293-1.414 1.414L10 18.414l9.707-9.707-1.414-1.414z" />{' '}
    </svg>
  );
}
