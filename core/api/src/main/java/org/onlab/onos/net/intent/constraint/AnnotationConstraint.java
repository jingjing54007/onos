/*
 * Copyright 2014 Open Networking Laboratory
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.onlab.onos.net.intent.constraint;

import com.google.common.base.MoreObjects;
import org.onlab.onos.net.Link;
import org.onlab.onos.net.resource.LinkResourceService;

import java.util.Objects;

/**
 * Constraint that evaluates an arbitrary link annotated value is under the specified threshold.
 */
public class AnnotationConstraint extends BooleanConstraint {

    private final String key;
    private final double threshold;

    /**
     * Creates a new constraint to keep the value for the specified key
     * of link annotation under the threshold.
     *
     * @param key key of link annotation
     * @param threshold threshold value of the specified link annotation
     */
    public AnnotationConstraint(String key, double threshold) {
        this.key = key;
        this.threshold = threshold;
    }

    /**
     * Returns the key of link annotation this constraint designates.
     * @return key of link annotation
     */
    public String key() {
        return key;
    }

    /**
     * Returns the threshold this constraint ensures as link annotated value.
     *
     * @return threshold as link annotated value
     */
    public double threshold() {
        return threshold;
    }

    @Override
    public boolean isValid(Link link, LinkResourceService resourceService) {
        double value = getAnnotatedValue(link, key);

        return value <= threshold;
    }

    /**
     * Returns the annotated value of the specified link. The annotated value
     * is expected to be String that can be parsed as double. If parsing fails,
     * the returned value will be 1.0.
     *
     * @param link link whose annotated value is obtained
     * @param key key of link annotation
     * @return double value of link annotation for the specified key
     */
    private double getAnnotatedValue(Link link, String key) {
        double value;
        try {
            value = Double.parseDouble(link.annotations().value(key));
        } catch (NumberFormatException e) {
            value = 1.0;
        }
        return value;
    }

    @Override
    public double cost(Link link, LinkResourceService resourceService) {
        if (isValid(link, resourceService)) {
            return getAnnotatedValue(link, key);
        } else {
            return -1;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, threshold);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof AnnotationConstraint)) {
            return false;
        }

        final AnnotationConstraint other = (AnnotationConstraint) obj;
        return Objects.equals(this.key, other.key) && Objects.equals(this.threshold, other.threshold);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("key", key)
                .add("threshold", threshold)
                .toString();
    }
}
