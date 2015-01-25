package edu.wpi.first.wpilibj;

public class Joystick extends GenericHID {

	static final byte kDefaultXAxis = 0;
    static final byte kDefaultYAxis = 1;
    static final byte kDefaultZAxis = 2;
    static final byte kDefaultTwistAxis = 2;
    static final byte kDefaultThrottleAxis = 3;
    static final int kDefaultTriggerButton = 1;
    static final int kDefaultTopButton = 2;

    /**
     * Represents an analog axis on a joystick.
     */
    public static class AxisType {

        /**
         * The integer value representing this enumeration
         */
        public final int value;
        static final int kX_val = 0;
        static final int kY_val = 1;
        static final int kZ_val = 2;
        static final int kTwist_val = 3;
        static final int kThrottle_val = 4;
        static final int kNumAxis_val = 5;
        /**
         * axis: x-axis
         */
        public static final AxisType kX = new AxisType(kX_val);
        /**
         * axis: y-axis
         */
        public static final AxisType kY = new AxisType(kY_val);
        /**
         * axis: z-axis
         */
        public static final AxisType kZ = new AxisType(kZ_val);
        /**
         * axis: twist
         */
        public static final AxisType kTwist = new AxisType(kTwist_val);
        /**
         * axis: throttle
         */
        public static final AxisType kThrottle = new AxisType(kThrottle_val);
        /**
         * axis: number of axis
         */
        public static final AxisType kNumAxis = new AxisType(kNumAxis_val);

        private AxisType(int value) {
            this.value = value;
        }
    }

    /**
     * Represents a digital button on the JoyStick
     */
    public static class ButtonType {

        /**
         * The integer value representing this enumeration
         */
        public final int value;
        static final int kTrigger_val = 0;
        static final int kTop_val = 1;
        static final int kNumButton_val = 2;
        /**
         * button: trigger
         */
        public static final ButtonType kTrigger = new ButtonType((kTrigger_val));
        /**
         * button: top button
         */
        public static final ButtonType kTop = new ButtonType(kTop_val);
        /**
         * button: num button types
         */
        public static final ButtonType kNumButton = new ButtonType((kNumButton_val));

        private ButtonType(int value) {
            this.value = value;
        }
    }
    private DriverStation m_ds;
    private final int m_port;
    private final byte[] m_axes;
    private final byte[] m_buttons;
    
    /**
     * Construct an instance of a joystick.
     * The joystick index is the usb port on the drivers station.
     *
     * @param port The port on the driver station that the joystick is plugged into.
     */
    public Joystick(final int port) {
        this(port, AxisType.kNumAxis.value, ButtonType.kNumButton.value);

        m_axes[AxisType.kX.value] = kDefaultXAxis;
        m_axes[AxisType.kY.value] = kDefaultYAxis;
        m_axes[AxisType.kZ.value] = kDefaultZAxis;
        m_axes[AxisType.kTwist.value] = kDefaultTwistAxis;
        m_axes[AxisType.kThrottle.value] = kDefaultThrottleAxis;

        m_buttons[ButtonType.kTrigger.value] = kDefaultTriggerButton;
        m_buttons[ButtonType.kTop.value] = kDefaultTopButton;

    }

    /**
     * Protected version of the constructor to be called by sub-classes.
     *
     * This constructor allows the subclass to configure the number of constants
     * for axes and buttons.
     *
     * @param port The port on the driver station that the joystick is plugged into.
     * @param numAxisTypes The number of axis types in the enum.
     * @param numButtonTypes The number of button types in the enum.
     */
    protected Joystick(int port, int numAxisTypes, int numButtonTypes) {
        m_ds = DriverStation.getInstance();
        m_axes = new byte[numAxisTypes];
        m_buttons = new byte[numButtonTypes];
        m_port = port;
    }
    
	@Override
	public double getX(Hand hand) {
		return getRawAxis(m_axes[AxisType.kX.value]);
	}

	@Override
	public double getY(Hand hand) {
		return getRawAxis(m_axes[AxisType.kY.value]);
	}

	@Override
	public double getZ(Hand hand) {
		return getRawAxis(m_axes[AxisType.kZ.value]);
	}

	@Override
	public double getTwist() {
		return getRawAxis(m_axes[AxisType.kTwist.value]);
	}

	@Override
	public double getThrottle() {
		return getRawAxis(m_axes[AxisType.kThrottle.value]);
	}

	@Override
	public double getRawAxis(int axis) {
		//0-based port
		return m_ds.getStickAxis(m_port, axis);
	}
	
	/**
     * For the current joystick, return the axis determined by the argument.
     *
     * This is for cases where the joystick axis is returned programatically, otherwise one of the
     * previous functions would be preferable (for example getX()).
     *
     * @param axis The axis to read.
     * @return The value of the axis.
     */
    public double getAxis(final AxisType axis) {
        switch (axis.value) {
        case AxisType.kX_val:
            return getX();
        case AxisType.kY_val:
            return getY();
        case AxisType.kZ_val:
            return getZ();
        case AxisType.kTwist_val:
            return getTwist();
        case AxisType.kThrottle_val:
            return getThrottle();
        default:
            return 0.0;
        }
    }

	@Override
	public boolean getTrigger(Hand hand) {
		return getRawButton(m_buttons[ButtonType.kTrigger.value]);
	}

	@Override
	public boolean getTop(Hand hand) {
		return getRawButton(m_buttons[ButtonType.kTop.value]);
	}

	@Override
	public boolean getBumper(Hand hand) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean getRawButton(int button) {
		return m_ds.getStickButton(m_port, button - 1);
	}

	@Override
	public int getPOV(int pov) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	/**
     * Get buttons based on an enumerated type.
     *
     * The button type will be looked up in the list of buttons and then read.
     *
     * @param button The type of button to read.
     * @return The state of the button.
     */
    public boolean getButton(ButtonType button) {
        switch (button.value) {
        case ButtonType.kTrigger_val:
            return getTrigger();
        case ButtonType.kTop_val:
            return getTop();
        default:
            return false;
        }
    }

    /**
     * Get the magnitude of the direction vector formed by the joystick's
     * current position relative to its origin
     *
     * @return The magnitude of the direction vector
     */
    public double getMagnitude() {
        return Math.sqrt(Math.pow(getX(), 2) + Math.pow(getY(), 2));
    }

    /**
     * Get the direction of the vector formed by the joystick and its origin
     * in radians
     *
     * @return The direction of the vector in radians
     */
    public double getDirectionRadians() {
        return Math.atan2(getX(), -getY());
    }

    /**
     * Get the direction of the vector formed by the joystick and its origin
     * in degrees
     *
     * uses acos(-1) to represent Pi due to absence of readily accessable Pi
     * constant in C++
     *
     * @return The direction of the vector in degrees
     */
    public double getDirectionDegrees() {
        return Math.toDegrees(getDirectionRadians());
    }

    /**
     * Get the channel currently associated with the specified axis.
     *
     * @param axis The axis to look up the channel for.
     * @return The channel fr the axis.
     */
    public int getAxisChannel(AxisType axis) {
        return m_axes[axis.value];
    }

    /**
     * Set the channel associated with a specified axis.
     *
     * @param axis The axis to set the channel for.
     * @param channel The channel to set the axis to.
     */
    public void setAxisChannel(AxisType axis, int channel) {
        m_axes[axis.value] = (byte) channel;
    }
}
