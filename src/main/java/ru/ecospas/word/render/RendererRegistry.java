package ru.ecospas.word.render;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.ecospas.word.blocks.Block;

import java.util.List;

@Component
@RequiredArgsConstructor
public class RendererRegistry {

    private final List<BlockRenderer<?>> renderers;

    public BlockRenderer<?> resolve(Block block) {

        for (BlockRenderer<?> renderer : renderers) {
            if (renderer.supports(block)) {
                return renderer;
            }
        }

        return null;
    }
}