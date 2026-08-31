export default function ErrorAlert({ message }: { message: string }) {
  return (
    <div className="error-alert" role="alert">
      <strong>Oups !</strong> {message}
    </div>
  )
}
