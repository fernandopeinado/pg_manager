package br.com.cas10.pgman.index;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

@Component
public class ExportQueue {

    @Value("${pgman.exportqueue.size:100}")
    private int exportQueueSize;
    private BlockingDeque<IndexedContent[]> queue;

    @PostConstruct
    public void init() {
        this.queue = new LinkedBlockingDeque<>(exportQueueSize);
    }

    public void add(IndexedContent... content) {
        if (content != null && content.length > 0) {
            if (!queue.offerLast(content)) {
                System.out.println("Despresando Conteudo " + content[0].getIndexNamePrefix() + " " + content[0].getTimestamp());
            }
        }
    }

    public IndexedContent[] take() throws InterruptedException {
        return queue.takeFirst();
    }

    public int size() {
        return queue.size();
    }

    public float ocuppancy() {
        return ((float) queue.size()) / ((float) exportQueueSize);
    }

    public void flush() {
        queue.clear();
    }

}
