interface StatusScreenProps {
  isLoading: boolean;
  isError: boolean;
  onRetry?: () => void; 
}

export default function StatusScreen({ isLoading, isError, onRetry }: StatusScreenProps) {
  
  // LOADING (Render Cold Start)
  if (isLoading) {
    return (
      <div className="status-container">
        <div style={{ textAlign: 'center' }}>
          <h2 className="status-title"> Waking up the server...</h2>
          <p className="status-text">
            The backend is hosted on a free instance (Render.com) and may take up to 60 seconds to start. Please wait.
          </p>
          <div className="loader"></div>
        </div>
      </div>
    );
  }

  // ERROR
  if (isError) {
    return (
      <div className="status-container">
        <div style={{ textAlign: 'center' }}>
          <h1 style={{ color: '#ff4444', fontSize: '2.5rem' }}>Connection Error:(</h1>
          <p className="status-text">Could not connect to the backend API</p>
          
          {onRetry && (
            <button onClick={onRetry} style={{ marginTop: '20px' }}>
              Try Again
            </button>
          )}
        </div>
      </div>
    );
  }

  return null;
}