package net.glowstone.shiny.event;

import com.google.common.base.Preconditions;
import org.spongepowered.api.event.Cancellable;
import org.spongepowered.api.event.Event;
import org.spongepowered.api.event.Result;

/**
 * A base class for defining events.
 */
public class BaseEvent implements Event {

    private Result result = Result.DEFAULT;

    public BaseEvent() {}

    @Override
    public boolean isCancellable() {
        return (this instanceof Cancellable);
    }

    @Override
    public void setResult(Result result) {
        Preconditions.checkNotNull(result, "result must not be null");
        this.result = result;
    }

    @Override
    public final Result getResult() {
        return result;
    }

}
