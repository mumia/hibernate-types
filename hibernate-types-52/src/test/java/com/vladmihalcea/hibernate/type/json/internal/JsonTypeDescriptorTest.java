package com.vladmihalcea.hibernate.type.json.internal;

import com.vladmihalcea.hibernate.type.model.BaseEntity;
import com.vladmihalcea.hibernate.type.util.Configuration;
import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

import static org.junit.Assert.assertTrue;

public class JsonTypeDescriptorTest {

    /**
     * If JSON serialization is used,
     * the {@link JsonTypeDescriptor#areEqual(Object, Object)} depends on the order of the elements.
     * <p>
     * If the first collection contains the all element of another collection,
     * then the two collection are equaled.
     * <p>
     * If the JSON object of the `theFirst` form would be :
     * {
     * "formFields":[1, 2, 3]
     * }
     * <p>
     * And, the JSON object of the `theSecond` form would be:
     * {
     * "formFields":[3, 2, 1]
     * }
     * <p>
     * The two JSON objects should be equal.
     */
    @Test
    public void testSetsAreEqual() {
        JsonTypeDescriptor descriptor = new JsonTypeDescriptor();

        Form theFirst = createForm(1, 2, 3);
        Form theSecond = createForm(3, 2, 1);
        assertTrue(descriptor.areEqual(theFirst, theSecond));
    }

    @Test
    public void testFromString() {
        JsonTypeDescriptor descriptor = new JsonTypeDescriptor(Configuration.INSTANCE.getObjectMapperWrapper());

        Object result = descriptor.fromString("{\"locale\":\"en-US\"}");

        assertTrue(result instanceof Object);
    }

    private Form createForm(Integer... numbers) {
        Form form = new Form();

        Set<FormField> formFields = new LinkedHashSet<>();

        Arrays.asList(numbers).forEach(o -> {
            FormField formField = new FormField();
            formField.setNumber(o);
            formFields.add(formField);
        });

        form.setFormFields(formFields);

        return form;
    }

    public static class FormField {

        private Integer number;

        public Integer getNumber() {
            return number;
        }

        public void setNumber(Integer number) {
            this.number = number;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            FormField formField = (FormField) o;
            return Objects.equals(number, formField.number);
        }

        @Override
        public int hashCode() {
            return Objects.hash(number);
        }
    }

    public static class Form extends BaseEntity {

        private Set<FormField> formFields;

        public Set<FormField> getFormFields() {
            return formFields;
        }

        public void setFormFields(Set<FormField> formFields) {
            this.formFields = formFields;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Form)) return false;
            Form form = (Form) o;
            return Objects.equals(formFields, form.formFields);
        }

        @Override
        public int hashCode() {
            return Objects.hash(formFields);
        }
    }
}
