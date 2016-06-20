# ProducerConsumerThreaded
Exits with 0 result only if it puts to queue all 10 items using while:
LinkProducer:40:13: while (!(inserted = queue.offer(value))) {sleep}

If there is no while - it will fill up the queue (5 items) then consumer will take those 5 and programm hang out.

Why?
