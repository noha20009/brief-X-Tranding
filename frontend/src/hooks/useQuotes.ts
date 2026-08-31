import { useEffect, useRef, useState } from 'react'

export interface Quote {
  assetId: number
  code: string
  nom: string
  type: string
  prix: number
  variationPct: number
}

export function useQuotes(): Map<number, Quote> {
  const [quotes, setQuotes] = useState<Map<number, Quote>>(new Map())
  const wsRef = useRef<WebSocket | null>(null)

  useEffect(() => {
    let disposed = false

    const connect = () => {
      const proto = window.location.protocol === 'https:' ? 'wss' : 'ws'
      const ws = new WebSocket(`${proto}://${window.location.host}/ws/quotes`)
      wsRef.current = ws

      ws.onmessage = (event) => {
        if (disposed) return
        try {
          const list = JSON.parse(event.data) as Quote[]
          setQuotes(new Map(list.map((q) => [q.assetId, q])))
        } catch {
          // ignore malformed payloads
        }
      }

      ws.onclose = () => {
        if (!disposed) {
          setTimeout(connect, 3000)
        }
      }
    }

    connect()

    return () => {
      disposed = true
      wsRef.current?.close()
    }
  }, [])

  return quotes
}
