# Bitácora de decisiones

Cada fila es una decisión de diseño que tuvo costo, con la alternativa que se descartó y el requisito de calidad al que apunta.

| Fecha | Decisión | Alternativas descartadas | Por qué | Requisito |
|---|---|---|---|---|
| 2026-11-15 | Guardar la tabla de posiciones ya calculada (materializada) en la base de estadisticas-service y actualizarla cada vez que llega un `PartidoFinalizado`. | Calcular la tabla en cada consulta a partir de todos los partidos. Recalcularla con una tarea programada cada cierto tiempo. | Calcular en cada consulta recorre todos los partidos en cada petición y con 30 clientes al tiempo es difícil cumplir el p95 < 300 ms. La tarea programada deja la tabla desactualizada entre ejecuciones y no cumple que se actualice en menos de 5 s. Con la tabla guardada la consulta es una lectura simple. | RQ-01 Desempeño |
| 2026-11-15 | Consumo idempotente de `PartidoFinalizado`: estadisticas-service guarda el `partidoId` de cada evento procesado y, si llega uno con un id ya registrado, lo descarta. Guardar el id y actualizar la tabla se hace en la misma transacción. | Confiar en que RabbitMQ entrega cada mensaje una sola vez. | RabbitMQ garantiza entrega al menos una vez, así que un mensaje puede llegar repetido (reintentos, caída del consumidor antes del ack). Sin control, el mismo partido sumaría puntos dos veces y la tabla quedaría mal sin que nadie lo note. Guardar el id cuesta una consulta por evento, que es poco frente a ese riesgo. | RQ-01 Desempeño |
