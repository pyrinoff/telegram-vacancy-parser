package ru.pyrinoff.chatjobparser.endpoint;

import org.jetbrains.annotations.NotNull;
import org.springframework.web.bind.annotation.*;
import ru.pyrinoff.chatjobparser.model.endpoint.*;
import ru.pyrinoff.chatjobparser.util.BytesFormatUtil;

import java.util.ArrayList;
import java.util.Date;

@RestController
@RequestMapping("/api")
public class TestEndpoint {

   @GetMapping("/info")
    private String info() {
           final Runtime runtime = Runtime.getRuntime();
           final long availableProcessors = runtime.availableProcessors();
           final long freeMemory = runtime.freeMemory();
           final String freeMemoryFormat = BytesFormatUtil.format(freeMemory);
           final long maxMemory = runtime.maxMemory();
           final boolean isMaxMemory = maxMemory == Long.MAX_VALUE;
           final String maxMemoryFormat = isMaxMemory ? "no limit" : BytesFormatUtil.format(maxMemory);
           final long totalMemory = runtime.totalMemory();
           final String totalMemoryFormat = BytesFormatUtil.format(totalMemory);
           final long usageMemory = totalMemory - freeMemory;
           final String usageMemoryFormat = BytesFormatUtil.format(usageMemory);

           @NotNull final StringBuilder sb = new StringBuilder();
           sb.append("[SYSTEM INFO]");
           sb.append("Cores: " + availableProcessors);
           sb.append("Free memory: " + freeMemoryFormat);
           sb.append("Maximum memory: " + maxMemoryFormat);
           sb.append("Total memory available to JVM: " + totalMemoryFormat);
           sb.append("Usage: " + usageMemoryFormat);
           return sb.toString();
   }

}
